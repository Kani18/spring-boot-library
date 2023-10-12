package com.luv2code.springbootlibrary.controller;

/*import com.okta.jwt.*;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.okta.jwt.JwtVerifiers;

import java.time.Duration;
import java.util.Scanner;

@RestController
@RequestMapping("/api/books")
public class JwtTokenController {

    @GetMapping("/secure/validate-token")
    public ResponseEntity<String> validateToken(@RequestParam String token) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the issuer: ");
        String issuer = scanner.nextLine();

        System.out.print("Enter the audience: ");
        String audience = scanner.nextLine();

        AccessTokenVerifier jwtVerifier = JwtVerifiers.accessTokenVerifierBuilder()
                //.setIssuer("https://dev-86703467.okta.com/oauth2/default")
                //.setAudience("api://default")
                .setIssuer(issuer)
                .setAudience(audience)
                .setConnectionTimeout(Duration.ofSeconds(4))
                .build();

        Jwt jwt = null;

        try {
            jwt = jwtVerifier.decode(token);
           // System.out.println("Claims: "+ jwt.getClaims() );
           // System.out.println("Token: " +jwt.getTokenValue());
            return ResponseEntity.status(HttpStatus.OK).body("Token validated");
        } catch (JwtVerificationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
    }
}*/



import com.okta.jwt.*;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.okta.jwt.JwtVerifiers;

import java.time.Duration;

@RestController
@RequestMapping("/api/books")
public class JwtTokenController {

    @Value("${JWT_ISSUER}")
    private String jwtIssuer;

    @Value("${JWT_AUDIENCE}")
    private String jwtAudience;
    @PostMapping("/secure/validate-token")
    public ResponseEntity<String> validateToken(@RequestParam String token, @RequestParam String issuer, @RequestParam String audience) {
        AccessTokenVerifier jwtVerifier = JwtVerifiers.accessTokenVerifierBuilder()
                .setIssuer(issuer)
                .setAudience(audience)
                .setConnectionTimeout(Duration.ofSeconds(4))
                .build();

        Jwt jwt = null;

        try {
            jwt = jwtVerifier.decode(token);
            // System.out.println("Claims: "+ jwt.getClaims() );
            // System.out.println("Token: " +jwt.getTokenValue());
            return ResponseEntity.status(HttpStatus.OK).body("Token validated");
        } catch (JwtVerificationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
    }
}





















//the Okta SDK's AccessTokenVerifier is responsible for the signature validation as part of the overall JWT validation process. If the token's signature is valid, it proceeds to check other claims like issuer and audience, and if all checks pass, it considers the token valid. If any check fails, it's treated as an invalid token.
