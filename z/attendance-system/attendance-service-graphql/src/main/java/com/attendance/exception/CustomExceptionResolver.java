package com.attendance.exception;

import com.netflix.graphql.dgs.exceptions.DefaultDataFetcherExceptionHandler;
import com.netflix.graphql.types.errors.TypedGraphQLError;
import graphql.GraphQLError;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class CustomExceptionResolver implements DataFetcherExceptionHandler {

    private final DefaultDataFetcherExceptionHandler defaultHandler = new DefaultDataFetcherExceptionHandler();

    @Override
    public CompletableFuture<DataFetcherExceptionHandlerResult> handleException(DataFetcherExceptionHandlerParameters handlerParameters) {
        if(handlerParameters.getException() instanceof EntityNotFoundException) {
            Map<String, Object> debugInfo = new HashMap<>();
            debugInfo.put("somefield", "somevalue");

            GraphQLError graphqlError = TypedGraphQLError.INTERNAL.message("This custom thing went wrong!")
                    .debugInfo(debugInfo)
                    .path(handlerParameters.getPath()).build();
            return CompletableFuture.completedFuture(DataFetcherExceptionHandlerResult.newResult()
                    .error(graphqlError)
                    .build());
        } else {
            return defaultHandler.handleException(handlerParameters);
        }
    }

//    @Override
    public CompletableFuture<DataFetcherExceptionHandlerResult> handleException1(DataFetcherExceptionHandlerParameters handlerParameters) {
        if (handlerParameters.getException() instanceof EntityNotFoundException) {
            Map<String, Object> debugInfo = new HashMap<>();
            debugInfo.put("somefield", "somevalue");

            GraphQLError graphqlError = TypedGraphQLError.newInternalErrorBuilder()
                    .message("This custom thing went wrong!")
                    .debugInfo(debugInfo)
                    .path(handlerParameters.getPath()).build();

            DataFetcherExceptionHandlerResult result = DataFetcherExceptionHandlerResult.newResult()
                    .error(graphqlError)
                    .build();

            return CompletableFuture.completedFuture(result);
        } else {
            return new CompletableFuture<DataFetcherExceptionHandlerResult>();
        }
    }
}
