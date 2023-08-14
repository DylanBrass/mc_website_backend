package com.mc_website.apigateway.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mc_website.apigateway.domainclientlayer.User.UserServiceClient;
import com.mc_website.apigateway.presentation.User.UserResponseModel;
import io.jsonwebtoken.JwtException;
import io.netty.handler.codec.http.HttpResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

import static com.jayway.jsonpath.internal.Utils.isEmpty;


@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserServiceClient userServiceClient;

    private final ObjectMapper objectMapper;

    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil,
                          UserServiceClient userServiceClient, ObjectMapper objectMapper) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userServiceClient = userServiceClient;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {



            // Get authorization header and validate
            final Cookie[] cookies = request.getCookies();

                if(cookies == null)
                {
                    chain.doFilter(request, response);
                return;
            }
            Cookie sessionCookie = null;
            for( Cookie cookie : cookies ) {
                if( ( "Bearer" ).equals( cookie.getName() ) ) {
                    sessionCookie = cookie;
                    break;
                }
            }

        if(sessionCookie == null)
        {
            chain.doFilter(request, response);
            return;
        }

            // Get jwt token and validate
        assert sessionCookie != null;
        final String token = sessionCookie.getValue();


        try {

            if (!jwtTokenUtil.validateToken(token)) {
                resolver.resolveException(request, response, null, new InvalidBearerTokenException("Token is expired"));
                return;
            }

            // Get user identity and set it on the spring security context
            //todo implement error handling of client error
            UserResponseModel userResponseModel = userServiceClient
                    .getUserByEmail(jwtTokenUtil.getUsernameFromToken(token));

            UserDetails userDetails = new UserPrincipalImpl(userResponseModel);


            UsernamePasswordAuthenticationToken
                    authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null,
                    userDetails == null ?
                            List.of() : userDetails.getAuthorities()
            );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }
        catch (Exception ex){
            resolver.resolveException(request, response, null, ex);

        }
    }

}
