package reactive.gateway.gateway.Security;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.reactive.function.client.WebClient;
import reactive.gateway.gateway.Jwt.JwtUtils;
import reactive.gateway.gateway.Request.OAuth2UserRequest;
import reactive.gateway.gateway.Response.OAuth2UserResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;

@Component
public class OAuth2SuccessHandler  implements ServerAuthenticationSuccessHandler {

    private final WebClient webClient;
    private final JwtUtils jwtUtils;
    private final String frontendUrl;
    private final boolean secureCookie;
    private final Logger log = org.slf4j.LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    public OAuth2SuccessHandler(
            WebClient.Builder webClientBuilder,
            JwtUtils jwtUtils,
            @Value("${services.user-service}") String userServiceUrl,
            @Value("${app.frontend-url:http://localhost:3001}") String frontendUrl,
            @Value("${app.oauth-cookie-secure:false}") boolean secureCookie) {
        this.webClient = webClientBuilder.baseUrl(userServiceUrl).build();
        this.jwtUtils = jwtUtils;
        this.frontendUrl = frontendUrl;
        this.secureCookie = secureCookie;
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange,
                                              Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name  = oAuth2User.getAttribute("name");

        log.info("OAuth2 success for email: {}, name: {}", email, name);


        return webClient.post()
                .uri("/api/users/internal/oauth2")
                .bodyValue(new OAuth2UserRequest(email, name))
                .retrieve()
                .bodyToMono(OAuth2UserResponse.class)
                .flatMap(userResponse -> {
                    String jwt = jwtUtils.generateTokenFromId(userResponse.getUserId(), userResponse.getRole(),userResponse.getStatus());

                    ResponseCookie cookie = ResponseCookie.from("nexa_access_token", jwt)
                            .httpOnly(true)
                            .path("/")
                            .maxAge(Duration.ofHours(1))
                            .domain(".digitshop.shop")                            .secure(secureCookie)
                            .build();

                    var response = webFilterExchange.getExchange().getResponse();
                    response.addCookie(cookie);
                    response.setStatusCode(HttpStatus.FOUND);
                    String callbackUrl = UriComponentsBuilder
                            .fromUriString(frontendUrl)
                            .path("/auth/callback")
                            .queryParam("email", email)
                            .queryParam("name", name)
                            .queryParam("role", userResponse.getRole())
                            .build()
                            .encode()
                            .toUriString();
                    response.getHeaders().setLocation(URI.create(callbackUrl));
                    return response.setComplete();
                });
    }

}
