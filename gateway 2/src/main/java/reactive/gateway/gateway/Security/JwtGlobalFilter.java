package reactive.gateway.gateway.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtGlobalFilter implements WebFilter{
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String token = extractToken(exchange);

        if (token == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(
                            Keys.hmacShaKeyFor(
                                    Decoders.BASE64.decode(jwtSecret)
                            )
                    )
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();
            String role = claims.get("role", String.class);

            var authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + role)
            );

            Authentication auth =
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            authorities
                    );

            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(r -> r
                            .headers(headers -> {
                                headers.remove("X-User-Id");
                            })
                            .header("X-User-Id", userId)

                    )
                    .build();

            return chain.filter(mutatedExchange)
                    .contextWrite(
                            ReactiveSecurityContextHolder.withAuthentication(auth)
                    );

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private String extractToken(ServerWebExchange exchange) {

        HttpCookie cookie = exchange.getRequest()
                .getCookies()
                .getFirst("token");

        if (cookie != null) {
            return cookie.getValue();
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }
}
