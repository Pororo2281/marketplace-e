package reactive.gateway.gateway;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import reactor.core.publisher.Mono;

@Configuration
public class MyKeyResolver {

    @Primary
    @Bean("ipKeyResolver")
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(
                exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
        );
    }

    @Bean("userKeyResolver")
    public KeyResolver userKeyResolver() {
        return exchange -> {
            String userId = exchange.getRequest()
                    .getHeaders()
                    .getFirst("X-USER-ID");
            return Mono.just(userId != null ? userId : "anonymous");
        };
    }


}
