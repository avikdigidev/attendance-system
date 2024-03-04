package com.attendance.apigateway.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Objects;

import org.springframework.web.server.ResponseStatusException;


import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter {

	@Autowired
	HttpTransport transport;
	@Autowired
	JsonFactory jsonFactory;
	@Value("${google.oauth.clientId}")
	private String clientId;
	private Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

	@Autowired
	private final ObjectMapper objectMapper;

	public LoggingFilter(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange,
							 GatewayFilterChain chain) {
		RequestPath requestPath = exchange.getRequest().getPath();
		logger.info("Path of the request received -> {} ",
				requestPath);
		if (requestPath.value().contains("/api/attendance/getAttendance")) {
			logger.info("Checking security for rest request -> {} ", requestPath);

			String path = Objects.requireNonNull(exchange.getRequest().getURI().getPath());
              String pathVariable = UriComponentsBuilder.fromPath(path)
                    .build()
                    .getPathSegments()
                    .get(3);

			  extractedMethod(exchange, pathVariable);

		} else if (requestPath.value().contains("/attendance-system-graphql/graphql")) {

			logger.info("Checking security for graphql request -> {} ", requestPath);

			ServerHttpRequest request = exchange.getRequest();

			if (isGraphQLRequest(request)) {
				String requestBody = request.getBody()
						.map(dataBuffer -> dataBuffer.asInputStream(true))
						.map(inputStream -> {
							try (inputStream) {
								return new String(inputStream.readAllBytes());
							} catch (IOException e) {
								logger.error("Error occurred during processing of graphql request -> {} ", e.getMessage());
								return "";
							}
						}).blockFirst();

				String employeeId = extractEmployeeId(requestBody);

				extractedMethod(exchange, employeeId);
			}
		}
		return chain.filter(exchange);
	}

	/*
	* this method gets token for authorization from header if header does not contains any token it throw appropriate error.
	* */
	private void extractedMethod(ServerWebExchange exchange, String employeeId) {
		HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
		if (httpHeaders.containsKey("Authorization")) {
			String token = httpHeaders.getFirst("Authorization");
			try {
				verifyToken(employeeId, token);
			} catch (Exception e) {
				logger.error("Error occurred during processing of token -> {} ", e.getMessage());
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
			}
		} else {
			logger.error("Invalid token");
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Token");
		}
	}

	/*
	* this method checks request is graphql request and method post or not.
	* */
	private boolean isGraphQLRequest(ServerHttpRequest request) {
		return request.getURI().getPath().endsWith("/graphql") && request.getMethod().matches("POST");
	}

	/*
	* this method extract employee id from graphql request body
	* */
	private String extractEmployeeId(String requestBody) {
		try {
			JsonNode jsonNode = objectMapper.readTree(requestBody);
			JsonNode variablesNode = jsonNode.path("variables");
			return variablesNode.path("employeeId").asText();
		} catch (IOException e) {
			return null;
		}
	}

	/*
	* this method verify token and if it's not valid throw unauthorized exception. else pass the request further to process.
	* */
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
