package order_service.order.Feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import order_service.order.Exception.*;
import org.apache.coyote.BadRequestException;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        String responseBody = response.body().toString();
        responseBody = responseBody==null? "Not response body" : responseBody;

        switch (response.status()) {
            case 400:
                return new BadRequestException(  String.format("Bad request from service. Status: %d, Body: %s",
                        response.status(), responseBody),
                        createThrowable(response));
            case 401:
                return new UnauthorizedException(String.format("Unauthorized in external service. Status: %d, Body: %s",
                        response.status(), responseBody),createThrowable(response));
            case 403:
                return new ForbiddenException(String.format("Forbidden in external service. Status: %d, Body: %s",
                        response.status(), responseBody),createThrowable(response));
            case 404:
                return new ResourceNotFoundException(String.format("Resource not found. Status: %d, Body: %s",
                        response.status(),responseBody),createThrowable(response));
            case 409:
                return new ConflictException(String.format("Conflict in external service. Status: %d, Body: %s",
                        response.status(),responseBody),createThrowable(response));
            case 422:
                return new UnprocessableEntityException(String.format("Unprocessable entity in external service. Status: %d, Body: %s",
                        response.status(),responseBody),createThrowable(response));
            case 500:
                return new InternalServerException(String.format("External service error. Status: %d, Body: %s"
                        ,response.status(),responseBody),createThrowable(response));
            default:
                return new Exception(String.format("Status: %d , Body: %s",
                        response.status(),responseBody),createThrowable(response));
        }
    }

    private Throwable createThrowable(Response response) {
        return new RuntimeException(String.format(
                "Original error: status=%d, reason=%s, request=%s",
                response.status(), response.reason(), response.request()
        ));
    }
}
