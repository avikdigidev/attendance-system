//package com.attendance.apigateway.filter;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.io.IOException;
//
//@Component
//public class GraphQLFilter implements GlobalFilter {
//
//    @Autowired
//    private final ObjectMapper objectMapper;
//
//    public GraphQLFilter(ObjectMapper objectMapper) {
//        this.objectMapper = objectMapper;
//    }
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//
//        if (isGraphQLRequest(request)) {
////            Mono<String> requestBodyMono = request.getBody().map(dataBuffer -> dataBuffer.asInputStream(true))
////                    .map(inputStream -> {
////                        try (inputStream) {
////                            return new String(inputStream.readAllBytes());
////                        } catch (IOException e) {
////                            return "";
////                        }
////                    }).blockFirst();
//
////            return requestBodyMono.flatMap(requestBody -> {
////                String employeeId = extractEmployeeId(requestBody);
////                exchange.getAttributes().put("employeeId", employeeId);
////            });
//
//            String requestBody = request.getBody()
//                    .map(dataBuffer -> dataBuffer.asInputStream(true))
//                    .map(inputStream -> {
//                        try (inputStream) {
//                            return new String(inputStream.readAllBytes());
//                        } catch (IOException e) {
//                            return "";
//                        }
//                    }).blockFirst();
//
//            String employeeId = extractEmployeeId(requestBody);
//        }
//        return chain.filter(exchange);
//    }
//
//    private boolean isGraphQLRequest(ServerHttpRequest request) {
//        return request.getURI().getPath().endsWith("/graphql") && request.getMethod().matches("POST");
//    }
//
//    private String extractEmployeeId(String requestBody) {
//        try {
//            JsonNode jsonNode = objectMapper.readTree(requestBody);
//            JsonNode variablesNode = jsonNode.path("variables");
//            return variablesNode.path("employeeId").asText();
//        } catch (IOException e) {
//            return null;
//        }
//    }
//}