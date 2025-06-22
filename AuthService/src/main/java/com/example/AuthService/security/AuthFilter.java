package com.example.AuthService.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final SecurityService securityService;
    private final HandlerExceptionResolver handlerExceptionResolver;


    public AuthFilter(JwtService jwtService, SecurityService securityService, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.securityService = securityService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");
            String path = request.getServletPath();
            String token = null;
            String username = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                if(path.contains("/refresh-token")){
                        filterChain.doFilter(request,response);
                        return;
                }
                username = jwtService.extractUser(token);
            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails user = securityService.loadUserByUsername(username);
                if (jwtService.validateToken(token, user)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        }catch (ExpiredJwtException exception){
                sendUnauthorizedResponse(response,"TOKEN SÜRESİ DOLMUŞ");
        }catch (Exception exception){
                handlerExceptionResolver.resolveException(request,response,null,exception);
        }
    }


    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write("{ \"error\": \"Unauthorized\", \"message\": \"" + message + "\" }");
        writer.flush();
    }
}
