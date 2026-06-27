package reactive.gateway.gateway.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
public class JwtGlobalFilter implements WebFilter{

    @Value("${jwt.secret}")
    private String jwtSecret;

    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public JwtGlobalFilter(CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String path = exchange.getRequest()
                .getURI()
                .getPath();

        if (
                path.startsWith("/api/auth/") ||
                        path.startsWith("/oauth2/") ||
                        path.startsWith("/login/oauth2/") ||
                        path.startsWith("/api/products/") ||
                        path.startsWith("/api/categories/") ||
                        path.startsWith("/api/search/")
        ) {
            return chain.filter(exchange);
        }

        String token = extractToken(exchange);

        if (token == null) {
            return customAuthenticationEntryPoint.commence(
                    exchange,
                    new InsufficientAuthenticationException("No JWT token provided")
            );
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
            String status = claims.get("status", String.class);

            if (!"ACTIVE".equals(status)) {
                return writeError(
                        exchange,
                        HttpStatus.FORBIDDEN,
                        "User is blocked"
                );
            }

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
                .getFirst("nexa_access_token");

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

    private Mono<Void> writeError(
            ServerWebExchange exchange,
            HttpStatus status,
            String message) {

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders()
                .setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "message", message,
                "status", status.value(),
                "timestamp", Instant.now(),
                "path", exchange.getRequest().getPath().value()
        );

        try {
            byte[] bytes = new ObjectMapper()
                    .writeValueAsBytes(body);

            DataBuffer buffer = exchange.getResponse()
                    .bufferFactory()
                    .wrap(bytes);

            return exchange.getResponse()
                    .writeWith(Mono.just(buffer));

        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
