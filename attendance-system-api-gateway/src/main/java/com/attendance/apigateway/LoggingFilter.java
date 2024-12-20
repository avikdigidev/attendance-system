package com.attendance.apigateway;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
public class LoggingFilter implements GlobalFilter {

	@Autowired
	HttpTransport transport;
	@Autowired
	JsonFactory jsonFactory;
	@Value("${google.oauth.clientId}")
	private String clientId;
	private Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange,
							 GatewayFilterChain chain) {
		RequestPath requestPath = exchange.getRequest().getPath();
		logger.info("Path of the request received -> {} ",
				requestPath);
//		if (requestPath.value().contains("/api/swipe")) {
			HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
			if (httpHeaders.containsKey("Authorization")) {
				String token = httpHeaders.getFirst("Authorization");
				try {
					verifyToken("", token);
				} catch (Exception e) {
					logger.error(e.getMessage());
					throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
				}
			} else {
				logger.error("Invalid token");
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Token");
			}
//		}
		return chain.filter(exchange);
	}
	private void verifyToken(String employeeId, String jwtToken) throws GeneralSecurityException, IOException {
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
				.setAudience(Collections.singletonList(clientId)).build();
		GoogleIdToken idToken = verifier.verify(jwtToken);
		if (idToken != null) {
			GoogleIdToken.Payload payload = idToken.getPayload();
			String userId = payload.getSubject();
			if(!employeeId.equals(userId)){
				logger.error("Invalid token");
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Token");
			}
		} else {
			logger.error("Invalid token");
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Token");
		}

	}
}
