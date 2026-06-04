package reactive.gateway.gateway.Security;

import io.netty.handler.timeout.ReadTimeoutException;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.webflux.autoconfigure.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.webflux.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.PrematureCloseException;

import java.net.ConnectException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Component
@Order(-2)
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

   public static final Logger log = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler(
            ErrorAttributes errorAttributes,
            ApplicationContext applicationContext,
            ServerCodecConfigurer serverCodecConfigurer) {

        super(errorAttributes,
                new WebProperties.Resources(),
                applicationContext);

        this.setMessageWriters(serverCodecConfigurer.getWriters());
        this.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(
                RequestPredicates.all(),
                this::renderErrorResponse
        );
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {

        log.error("Gateway error occurred", getError(request));

        Throwable ex = getError(request);

        Map<String, Object> errorAttributes = getErrorAttributes(
                request,
                ErrorAttributeOptions.defaults()

        );

        int statusCode = 500;

        if (ex instanceof TimeoutException
                || ex instanceof ReadTimeoutException) {
            statusCode = 504;
            errorAttributes.put("message", "Gateway timeout");
        }
        else if (ex instanceof PrematureCloseException) {
            statusCode = 502;
            errorAttributes.put("message", "Bad gateway");
        }
        else if (ex instanceof WebClientRequestException) {
            statusCode = 503;
            errorAttributes.put("message", "Service unreachable");
        }
        else if (ex instanceof ConnectException){
            statusCode = 503;
            errorAttributes.put("message", "Service is currently unavailable. Please try again later.");
        }

        return ServerResponse
                .status(statusCode)
                .bodyValue(Map.of(
                        "message", errorAttributes.getOrDefault("message", "Unexpected error"),
                        "status", statusCode,
                        "timestamp", Instant.now(),
                        "path", errorAttributes.getOrDefault("path", request.path())
                ));
    }


}
