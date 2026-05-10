package org.example.userservice.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.example.userservice.Exception.NotFoundById;
import org.example.userservice.Repository.UserRepo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JwtFilter extends OncePerRequestFilter {

    private JwtUtils jwtUtils;
    private UserDetailServiceImpl userDetailServiceImpl;
    private UserRepo userRepo;

    public JwtFilter(JwtUtils jwtUtils, UserDetailServiceImpl userDetailServiceImpl,UserRepo userRepo) {
        this.jwtUtils = jwtUtils;
        this.userDetailServiceImpl = userDetailServiceImpl;
        this.userRepo = userRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        if (path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response); // пропускаем auth маршруты
            return;
        }
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ")){
            token = token.substring(7);
            String id = jwtUtils.getIdFromJWTToken(token,"ACCESS");
            String username = userRepo.findById(Long.parseLong(id))
                    .map(x->x.getEmail())
                    .orElseThrow(()->new NotFoundById("user not found by id: " + id));

            if(username != null){
                var userDetails = userDetailServiceImpl.loadUserByUsername(username);
                if(jwtUtils.validateJwtToken(token)){
                    var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(userDetails);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
