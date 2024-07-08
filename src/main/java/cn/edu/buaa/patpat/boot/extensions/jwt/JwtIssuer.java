package cn.edu.buaa.patpat.boot.extensions.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtIssuer implements IJwtIssuer {
    private final int expiration;
    private final SecretKey key;

    public JwtIssuer(int expiration, String secret) {
        this.expiration = expiration;
        this.key = generateKey(secret);
    }

    private static SecretKey generateKey(String secret) {
        byte[] secretBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public String issue(String subject) {
        return Jwts.builder().subject(subject)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + expiration))
                .signWith(key)
                .compact();
    }

    @Override
    public String verify(String token) throws JwtVerifyException {
        try {
            return Jwts.parser()
                    .decryptWith(key)
                    .build()
                    .parseEncryptedClaims(token)
                    .getPayload().getSubject();
        } catch (io.jsonwebtoken.JwtException e) {
            throw new JwtVerifyException("JWT expired", e);
        } catch (IllegalArgumentException e) {
            throw new JwtVerifyException("Invalid JWT", e);
        }
    }
}
