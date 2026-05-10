package reactive.gateway.gateway.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtGlobalFilter implements WebFilter, Ordered {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();
            String role = claims.get("role", String.class);

            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

            var authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);

            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(exchange.getRequest()
                            .mutate()
                            .header("X-USER-ID", userId)
                            .header("X-USER-ROLE", role)
                            .build())
                    .build();

            System.out.println("JWT parsed");
            System.out.println("USER ID = " + userId);
            System.out.println("ROLE = " + role);


            return chain.filter(mutatedExchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }

}
