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
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    public SecurityConfig(JwtGlobalFilter jwtGlobalFilter, OAuth2SuccessHandler oAuth2SuccessHandler) {
        this.jwtGlobalFilter = jwtGlobalFilter;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable())
                .addFilterAt(jwtGlobalFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/oauth2/authorization/**").permitAll()
                        .pathMatchers("/login/oauth2/**").permitAll()
                        .pathMatchers("/internal/**").denyAll()
                        .pathMatchers("/api/auth/**").permitAll()
                        .pathMatchers("/api/categories/**").permitAll()
                        .pathMatchers("/api/products/**").permitAll()
                        .pathMatchers("/api/search/**").permitAll()
                        .pathMatchers("/api/sellers/*/products").permitAll()
                        .pathMatchers("/api/sellers/**").hasRole("SELLER")
                        .pathMatchers("/api/orders/sellers/**").hasRole("SELLER")
                        .anyExchange().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .authenticationSuccessHandler(oAuth2SuccessHandler)
                );

        return http.build();
    }
}