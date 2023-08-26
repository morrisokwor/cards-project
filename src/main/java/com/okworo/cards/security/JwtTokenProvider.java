package com.okworo.cards.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.okworo.cards.entities.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * @author Morris.Okworo on 26/08/2023
 */
@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private Long jwtExpirationInMs;

    public String createAuthToken(UserEntity user) {
        return createToken(user, jwtExpirationInMs);
    }

    public String createRefreshToken(UserEntity user) {
        return createToken(user, 259200000L);
    }

    private String createToken(UserEntity user, Long expireTime) {
        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime() + expireTime);

        return JWT.create()
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiredAt)
                .withSubject(user.getEmail())
                .sign(Algorithm.HMAC512(jwtSecret));
    }

    public String verifyToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(jwtSecret)).build().verify(token);
            return decodedJWT.getSubject();
        } catch (Exception ex) {
            return null;
        }
    }

}
