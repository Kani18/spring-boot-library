package com.luv2code.springbootlibrary.config;

import com.luv2code.springbootlibrary.filter.JwtValidationFilter;
import com.okta.spring.boot.oauth.Okta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

@Configuration

public class SecurityConfiguration {


    @Bean
    public JwtValidationFilter jwtFilter() {
        return new JwtValidationFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,JwtValidationFilter jwtFilter) throws Exception {
    //SecurityFilterChain-->bean that describes a chain of security rules(filters)

        // Disable Cross Site Request Forgery
        http.csrf().disable();

        // Protect endpoints at /api/<type>/secure
        http.authorizeRequests(configurer ->
                configurer
                        .antMatchers("/api/books/secure/validate-token").permitAll()
                        .antMatchers( "/api/books/**",
                                "/api/books/secure/**",
                                "/api/reviews/secure/**",
                                "/api/messages/secure/**",
                                "/api/admin/secure/**")
                        .authenticated())
                .oauth2ResourceServer()
                .jwt();



        // Add CORS filters
        http.cors();

        // Add content negotiation strategy
        http.setSharedObject(ContentNegotiationStrategy.class,
                new HeaderContentNegotiationStrategy());

        // Force a non-empty response body for 401's to make the response friendly
        Okta.configureResourceServer401ResponseBody(http);
        // Add a filter to validate the tokens with every request
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }



}







