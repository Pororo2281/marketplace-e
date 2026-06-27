package reactive.gateway.gateway.Config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

@Configuration
public class MyKeyResolver {

    @Primary
    @Bean("ipKeyResolver")
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            String ip = exchange.getRequest()
                    .getHeaders()
                    .getFirst("X-Forwarded-For");

            if (ip != null && !ip.isBlank()) {
                return Mono.just(ip.split(",")[0].trim());
            }

            var remote = exchange.getRequest().getRemoteAddress();

            if (remote != null) {
                return Mono.just(remote.getHostString());
            }

            return Mono.just("unknown");
        };
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
