package com.attendance.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {
	
	@Bean
	public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p
						.path("/get")
						.filters(f -> f
								.addRequestHeader("api-gateway-custom-header", "test value")
								)
						.uri("http://httpbin.org:80"))
				.route(p -> p.path("/attendance-event-store/**")
						.uri("lb://attendance-event-store"))
				.route(p -> p.path("/attendance-system-graphql/**")
						.uri("lb://attendance-system-graphql"))
				.route(p -> p.path("/attendance-system-service/**")
						.uri("lb://attendance-system-service"))
				.build();
	}

}


