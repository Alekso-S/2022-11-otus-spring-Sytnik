package ru.otus.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;
import ru.otus.spring.exception.BookAlreadyExistsEx;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.exception.CommentNotFoundEx;

import java.util.List;
import java.util.Map;

@Component
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalErrorWebExceptionHandler.class);

    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes,
                                          WebProperties.Resources resources,
                                          ApplicationContext applicationContext,
                                          ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(
            ServerRequest request) {

        Map<String, Object> errorPropertiesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        Throwable throwable = getError(request);
        if (List.of(
                BookNotFoundEx.class,
                CommentNotFoundEx.class,
                BookAlreadyExistsEx.class
        ).contains(throwable.getClass())) {
            errorPropertiesMap.replace("error", throwable.getMessage());
            logger.error(throwable.getMessage());
        } else {
            throwable.printStackTrace();
        }

        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorPropertiesMap));
    }
}
