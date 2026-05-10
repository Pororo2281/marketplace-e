package reactive.gateway.gateway;

import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;

@Configuration
public class RedisLimiter {
    @Bean
    public RedisRateLimiter redisRateLimiter(ReactiveRedisConnectionFactory redisConnectionFactory) {
        return new RedisRateLimiter(5, 20);
    }
}
