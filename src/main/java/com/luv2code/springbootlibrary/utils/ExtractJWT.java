package com.luv2code.springbootlibrary.utils;

/*import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ExtractJWT {

    public static String payloadJWTExtraction(String token, String extraction) {

        token.replace("Bearer ", "");

        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));

        String[] entries = payload.split(",");
        Map<String, String> map = new HashMap<String, String>();

        for (String entry : entries) {
            String[] keyValue = entry.split(":");
            if (keyValue[0].equals(extraction)) {

                int remove = 1;
                if (keyValue[1].endsWith("}")) {
                    remove = 2;
                }
                keyValue[1] = keyValue[1].substring(0, keyValue[1].length() - remove);
                keyValue[1] = keyValue[1].substring(1);

                map.put(keyValue[0], keyValue[1]);
            }
        }
        if (map.containsKey(extraction)) {
            return map.get(extraction);
        }
        return null;
    }
}*/




import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.util.ResourceRetriever;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.JSONValue;
import org.jose4j.json.internal.json_simple.parser.JSONParser;
import org.jose4j.json.internal.json_simple.parser.ParseException;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.resolvers.JwksVerificationKeyResolver;
import org.jose4j.keys.resolvers.VerificationKeyResolver;
import org.jose4j.lang.JoseException;





public class ExtractJWT {

  /*  public static JwtClaims validateToken(String token, String issuer, String jwksUri) throws InterruptedException, ExecutionException, IOException, InvalidJwtException, JoseException, ParseException {
        if (StringUtils.isEmpty(token)) {
            throw new IllegalArgumentException("Token cannot be empty or null");
        }
        if (StringUtils.isEmpty(issuer)) {
            throw new IllegalArgumentException("Issuer cannot be empty or null");
        }

        try {

            URL jwksUrl = new URL(jwksUri);
            String jwksJson = fetchJwksJson(jwksUrl);

            System.out.println(" JWKS JSON: " + jwksJson);
            jwksJson = jwksJson.trim().toString();

            // Parse the JWKS using JSONParser
            JSONParser parser = new JSONParser();
            JSONObject jwksObject = (JSONObject) parser.parse(jwksJson);

            // Convert parsed JWKS back to String for the next steps
            String jsonWebKeySetString = jwksObject.toJSONString();

            // Parse the JWKS JSON into a JsonWebKeySet using jose4j library
            JsonWebKeySet jsonWebKeySet = new JsonWebKeySet(jsonWebKeySetString);

            // Initialize the JWT consumer with a JwksVerificationKeyResolver
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime()
                    .setExpectedIssuer(issuer)
                    .setVerificationKeyResolver(new JwksVerificationKeyResolver(jsonWebKeySet.getJsonWebKeys()))
                    .setRelaxVerificationKeyValidation()
                    .setJwsAlgorithmConstraints(
                            new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST, "RS256"))
                    .build();

            // Process and validate the token
            return jwtConsumer.processToClaims(token);
        } catch (InvalidJwtException e) {
            e.printStackTrace();
            throw e;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }*/

   /* public static JwtClaims validateToken(String token, String issuer, String publicKey) throws InvalidJwtException, JoseException, ParseException {
        if (StringUtils.isEmpty(token)) {
            throw new IllegalArgumentException("Token cannot be empty or null");
        }
        if (StringUtils.isEmpty(issuer)) {
            throw new IllegalArgumentException("Issuer cannot be empty or null");
        }
         String jsonKey = "{\"keys\":[{\"kty\":\"RSA\",\"alg\":\"RS256\",\"kid\":\"RjzaWYHRun2KUaurcn2VG1--D1fqJdWuKQRy1ztfgPg\",\"use\":\"sig\",\"e\":\"AQAB\",\"n\":\"qGTmJfGDdz3bLdwSwg4SsZQcVbpO4jlaKx2vOp6bbWcRNT3TLkGCbMQn0dqPFyg85vMCdM4iYQZ4aTc_X6fv45HtO9v0TWxSh3HzOfhUT6-nzetM9MQK23bnUZZA823ZHYrBSQhqjNK9p_ESlf7sTkhlxj6ADt56WN5FwR_P7qvkIKac7WP6j3vj4VORMrD36bbZDyOJoAY7YJHIj5phxvVTMFxto_wFfwJn1bG-ssb2KJGxEKFGKQGkHeQXmbDJ5ZrVe3kTSPbRDD2bP24wHynoFuNuKu-StNx-npu3smJQZGLhavW6Z1XyjPTGQ9CXEuMrw_zbKUjOULuBrAwzrQ\"}]}";


        try {
            // Parse the JSON representation of the key into a JSONObject
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonKey);

            // Create an RsaJsonWebKey using the JSONObject
            RsaJsonWebKey jsonWebKey = new RsaJsonWebKey(jsonObject);

            // Initialize the JWT consumer with the provided JWK
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime()
                    .setExpectedIssuer(issuer)
                    .setVerificationKey(jsonWebKey.getKey())
                    .setRelaxVerificationKeyValidation()
                    .setJwsAlgorithmConstraints(
                            new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST, "RS256"))
                    .build();

            // Process and validate the token
            return jwtConsumer.processToClaims(token);
        } catch (InvalidJwtException e) {
            e.printStackTrace();
            throw e;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }*/


    public static JwtClaims validateToken(String token, String issuer, String publicKey) throws InvalidJwtException, JoseException, ParseException {
        if (StringUtils.isEmpty(token)) {
            throw new IllegalArgumentException("Token cannot be empty or null");
        }
        if (StringUtils.isEmpty(issuer)) {
            throw new IllegalArgumentException("Issuer cannot be empty or null");
        }
        String jsonKey = "{\"keys\":[{\"kty\":\"RSA\",\"alg\":\"RS256\",\"kid\":\"RjzaWYHRun2KUaurcn2VG1--D1fqJdWuKQRy1ztfgPg\",\"use\":\"sig\",\"e\":\"AQAB\",\"n\":\"qGTmJfGDdz3bLdwSwg4SsZQcVbpO4jlaKx2vOp6bbWcRNT3TLkGCbMQn0dqPFyg85vMCdM4iYQZ4aTc_X6fv45HtO9v0TWxSh3HzOfhUT6-nzetM9MQK23bnUZZA823ZHYrBSQhqjNK9p_ESlf7sTkhlxj6ADt56WN5FwR_P7qvkIKac7WP6j3vj4VORMrD36bbZDyOJoAY7YJHIj5phxvVTMFxto_wFfwJn1bG-ssb2KJGxEKFGKQGkHeQXmbDJ5ZrVe3kTSPbRDD2bP24wHynoFuNuKu-StNx-npu3smJQZGLhavW6Z1XyjPTGQ9CXEuMrw_zbKUjOULuBrAwzrQ\"}]}";

        try {
            // Parse the JSON representation of the key into a JSONObject
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonKey);

            RsaJsonWebKey jsonWebKey = new RsaJsonWebKey(jsonObject);

            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime()
                    .setExpectedIssuer(issuer)
                    .setVerificationKey(jsonWebKey.getKey())
                    .setRelaxVerificationKeyValidation()
                    .setJwsAlgorithmConstraints(
                            new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST, "RS256"))
                    .build();

            // Process and validate the token
            return jwtConsumer.processToClaims(token);

        } catch (InvalidJwtException e) {
            e.printStackTrace();
            throw e;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    public static String payloadJWTExtraction(String token, String extraction) {
        token = token.replace("Bearer ", ""); // Assign the replaced value back to 'token'

        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));

        String[] entries = payload.split(",");
        Map<String, String> map = new HashMap<>();

        for (String entry : entries) {
            String[] keyValue = entry.split(":");
            if (keyValue[0].equals(extraction)) {

                int remove = 1;
                if (keyValue[1].endsWith("}")) {
                    remove = 2;
                }
                keyValue[1] = keyValue[1].substring(0, keyValue[1].length() - remove);
                keyValue[1] = keyValue[1].substring(1);

                map.put(keyValue[0], keyValue[1]);
            }
        }
        if (map.containsKey(extraction)) {
            return map.get(extraction);
        }
        return null;
    }



    private static String fetchJwksJson(URL jwksUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) jwksUrl.openConnection();
        try (InputStream inputStream = connection.getInputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            StringBuilder stringBuilder = new StringBuilder();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                stringBuilder.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            }
            return stringBuilder.toString();
        } finally {
            connection.disconnect();
        }
    }
}







//JOSE is short for Javascript Object Signing and Encryption,
// which is the IETF Working Group that developed the JSON Web Signature (JWS), JSON Web Encryption (JWE) and JSON Web Key (JWK) specifications.
// JWS and JWE use JSON and base64url encoding to secure messages in a (relatively) simple, compact and web safe format
// while JWK defines a JSON representation of cryptographic keys.
// The actual algorithms for JWS, JWE and JWK are defined in JSON Web Algorithms (JWA).
// The library supports the JWS/JWE compact serializations with the complete suite of JOSE algorithms.