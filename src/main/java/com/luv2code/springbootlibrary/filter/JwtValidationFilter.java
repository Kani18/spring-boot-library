package com.luv2code.springbootlibrary.filter;

import com.okta.jwt.AccessTokenVerifier;
import com.okta.jwt.Jwt;
import com.okta.jwt.JwtVerificationException;
import com.okta.jwt.JwtVerifiers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;

public class JwtValidationFilter extends OncePerRequestFilter {

    @Value("${JWT_ISSUER}")
    private String jwtIssuer;

    @Value("${JWT_AUDIENCE}")
    private String jwtAudience;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Extract and validate the JWT token from the request
        String token = extractToken(request);
        if (validateToken(token)) {
            PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(null, token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        // Extract the token from the request header
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    private boolean validateToken(String token) throws AuthenticationException {
        AccessTokenVerifier jwtVerifier = JwtVerifiers.accessTokenVerifierBuilder()
                //.setIssuer("https://dev-86703467.okta.com/oauth2/default")
                //.setAudience("api://default")
                .setIssuer(jwtIssuer)
                .setAudience(jwtAudience)
                .setConnectionTimeout(Duration.ofSeconds(4))
                .build();

        try {
            Jwt jwt = jwtVerifier.decode(token);
           // System.out.println(jwt.getClaims() + " " + jwt.getTokenValue());
            return true;
        } catch (JwtVerificationException e) {
            // throw new AuthenticationException("Invalid token"){};
            return false;
        }
    }
}



