package reactive.gateway.gateway;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Router {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder,
                                     RedisRateLimiter redisRateLimiter,
                                     KeyResolver ipKeyResolver,
                                     KeyResolver userKeyResolver) {
        return builder.routes()


                .route("user-service-auth", r -> r.path("/api/auth/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri("http://localhost:8081"))


                .route("product-service-seller", r -> r.path("/api/sellers/products/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri("http://localhost:8082"))



                .route("user-service-seller", r -> r.path("/api/sellers/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri("http://localhost:8081"))

                .route("user-service-user", r -> r.path("/api/users/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri("http://localhost:8081"))


                .route("product-service", r -> r.path("/api/products/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri("http://localhost:8082"))


                .route("product-service-admin", r -> r.path("/api/admin/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri("http://localhost:8082"))


                .route("product-service-categories", r -> r.path("/api/categories/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri("http://localhost:8082"))


                .route("product-service-search", r -> r.path("/api/search/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri("http://localhost:8082"))


                .route("product-service-internal", r -> r.path("/api/internal/products/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri("http://localhost:8082"))

                .route("order-service-cart", r -> r.path("/api/cart", "/api/cart/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri("http://localhost:8083"))
                .route("order-service-seller", r -> r.path("/api/orders/sellers/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri("http://localhost:8083"))
                .route("order-service-order", r -> r.path("/api/orders/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri("http://localhost:8083"))
                .route("order-service-library", r -> r.path("/api/library/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri("http://localhost:8083"))
                .route("order-service-download", r -> r.path("/api/downloads/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri("http://localhost:8083"))
                .route("payment-service", r -> r.path("/api/payments/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri("http://localhost:8084"))
                .route("payment-service-yookassa-webhook", r -> r.path("/api/webhook/yookassa/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri("http://localhost:8084"))
                .route("review-service", r -> r.path("/api/reviews/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri("http://localhost:8088"))

                .build();
    }
}