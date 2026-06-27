package reactive.gateway.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Router {

    @Value("${services.user-service}")
    private String userServiceUrl;

    @Value("${services.product-service}")
    private String productServiceUrl;

    @Value("${services.order-service}")
    private String orderServiceUrl;

    @Value("${services.payment-service}")
    private String paymentServiceUrl;

    @Value("${services.admin-service}")
    private String adminServiceUrl;

    @Value("${services.review-service}")
    private String reviewServiceUrl;

    @Value("${services.search-service}")
    private String searchServiceUrl;

    @Value("${services.notification-service}")
    private String notificationServiceUrl;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder,
                                     RedisRateLimiter redisRateLimiter,
                                     KeyResolver ipKeyResolver,
                                     KeyResolver userKeyResolver) {
        return builder.routes()

                .route("oauth-start", r -> r.path("/oauth2/authorization/**")
                        .uri(userServiceUrl))

                .route("oauth-callback", r -> r.path("/login/oauth2/**")
                        .uri(userServiceUrl))

                .route("user-service-auth", r -> r.path("/api/auth/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri(userServiceUrl))

                .route("admin-service", r -> r.path("/api/admins/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri(adminServiceUrl))

                .route("product-service-seller", r -> r.path("/api/sellers/products/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri(productServiceUrl))

                .route("user-service-seller", r -> r.path("/api/sellers/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri(userServiceUrl))

                .route("user-service-user", r -> r.path("/api/users/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri(userServiceUrl))

                .route("product-service", r -> r.path("/api/products/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri(productServiceUrl))

                .route("product-service-admin", r -> r.path("/api/admin/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri(productServiceUrl))

                .route("product-service-categories", r -> r.path("/api/categories/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri(productServiceUrl))

                .route("product-service-search", r -> r.path("/api/search/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri(productServiceUrl))

                .route("search-service", r -> r.path("/api/search/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri(searchServiceUrl))

                .route("product-service-internal", r -> r.path("/api/internal/products/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri(productServiceUrl))

                .route("order-service-cart", r -> r.path("/api/cart", "/api/cart/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri(orderServiceUrl))

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
                        .uri(orderServiceUrl))

                .route("order-service-library", r -> r.path("/api/library/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri(orderServiceUrl))

                .route("order-service-download", r -> r.path("/api/downloads/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri(orderServiceUrl))

                .route("payment-service", r -> r.path("/api/payments/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri(paymentServiceUrl))

                .route("payment-service-yookassa-webhook", r -> r.path("/api/webhook/yookassa/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri(paymentServiceUrl))

                .route("review-service", r -> r.path("/api/reviews/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setRateLimiter(redisRateLimiter);
                            c.setKeyResolver(ipKeyResolver);
                        }))
                        .uri(reviewServiceUrl))

                .route("user-service-docs", r -> r.path("/user-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/user-service/(?<segment>.*)", "/${segment}"))
                        .uri(userServiceUrl))

                .route("admin-service-docs", r -> r.path("/admin-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/admin-service/(?<segment>.*)", "/${segment}"))
                        .uri(adminServiceUrl))

                .route("payment-service-docs", r -> r.path("/payment-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/payment-service/(?<segment>.*)", "/${segment}"))
                        .uri(paymentServiceUrl))

                .route("review-service-docs", r -> r.path("/review-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/review-service/(?<segment>.*)", "/${segment}"))
                        .uri(reviewServiceUrl))

                .route("search-service-docs", r -> r.path("/search-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/search-service/(?<segment>.*)", "/${segment}"))
                        .uri(searchServiceUrl))

                .route("notification-service-docs", r -> r.path("/notification-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/notification-service/(?<segment>.*)", "/${segment}"))
                        .uri(notificationServiceUrl))

                .route("product-service-docs", r -> r.path("/product-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/product-service/(?<segment>.*)", "/${segment}"))
                        .uri(productServiceUrl))

                .route("order-service-docs", r -> r.path("/order-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/order-service/(?<segment>.*)", "/${segment}"))
                        .uri(orderServiceUrl))

                .build();
    }
}