package com.example.ordermanagement.auth;

import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;


@Component
public class JwtKeyUtil {

    public static Key getKeyFromString(String secretKey) {
        // Decode the Base64 encoded string into a byte array
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);

        // Create a SecretKeySpec using the decoded byte array
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "HMACSHA256");
    }
}
