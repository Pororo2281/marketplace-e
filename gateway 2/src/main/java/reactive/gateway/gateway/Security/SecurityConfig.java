package reactive.gateway.gateway.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.server.WebFilter;

@Configuration
public class SecurityConfig {

    private final JwtGlobalFilter jwtGlobalFilter;

    public SecurityConfig(JwtGlobalFilter jwtGlobalFilter) {
        this.jwtGlobalFilter = jwtGlobalFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges -> exchanges
                                // публичные эндпоинты
                                .pathMatchers("/api/auth/**").permitAll()
                                .pathMatchers("/api/categories/**").permitAll()
                                .pathMatchers("/api/products/**").permitAll()
                                .pathMatchers("/api/search/**").permitAll()
                                .pathMatchers("/api/sellers/*/products").permitAll()
                                .pathMatchers("/api/sellers/**").hasRole("SELLER")
                                .pathMatchers("/api/orders/sellers/**").hasRole("SELLER")

                                // доступ только для ADMIN
//              .pathMatchers("/api/admin/**").hasRole("ADMIN")
                                .anyExchange().authenticated()
                ).addFilterBefore(jwtGlobalFilter,SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }
}