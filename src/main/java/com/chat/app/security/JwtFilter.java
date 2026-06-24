package com.chat.app.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Collections;
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService){

        this.jwtService = jwtService;

    }
    @Override
    protected void doFilterInternal(

            HttpServletRequest request,

            HttpServletResponse response,

            FilterChain filterChain

    )
            throws ServletException, IOException {

        String path =
                request.getRequestURI();

        System.out.println(
                "JWT FILTER RUNNING"
        );
        System.out.println(
                "REQUEST PATH = " + path
        );

        if(
                path.startsWith("/api/auth/")

                        ||

                        path.equals("/")

                        ||

                        path.equals("/auth.html")

                        ||

                        path.equals("/chat.html")

                        ||

                        path.startsWith("/css/")

                        ||

                        path.startsWith("/js/")

                        ||

                        path.equals("/favicon.ico")

                        ||

                        path.startsWith("/ws/")

                        ||

                        path.startsWith("/chat")
        ){


            filterChain.doFilter(
                    request,
                    response
            );


            return;

        }

        String authHeader =
                request.getHeader("Authorization");
        System.out.println(
                "AUTH HEADER = " + authHeader
        );

        if(
                authHeader == null

                        ||

                        !authHeader.startsWith("Bearer ")
        ){
            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );
            return;
        }
        String token =
                authHeader.substring(7);

        if(
                !jwtService.isTokenValid(token)
        ){
            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );


            return;

        }
        String userId =
                jwtService.extractUserId(token);
        UsernamePasswordAuthenticationToken authentication =


                new UsernamePasswordAuthenticationToken(

                        userId,

                        null,

                        Collections.singletonList(

                                new SimpleGrantedAuthority(
                                        "ROLE_USER"
                                )

                        )

                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        filterChain.doFilter(
                request,
                response
        );
    }
}