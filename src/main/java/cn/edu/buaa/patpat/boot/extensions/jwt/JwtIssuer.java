package cn.edu.buaa.patpat.boot.extensions.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.util.DigestUtils;

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
        // generate 192-bit MD5 from secret
        String md5 = DigestUtils.md5DigestAsHex(secret.getBytes());
        byte[] secretBytes = Decoders.BASE64.decode(md5);
        String md5Again = DigestUtils.md5DigestAsHex(secretBytes);
        byte[] secretBytesAgain = Decoders.BASE64.decode(md5Again);
        // blend the two md5 hashes to get a 256-bit key
        byte[] secretBytes256 = new byte[32];
        for (int i = 0; i < 16; i++) {
            secretBytes256[i] = secretBytes[i];
            secretBytes256[i + 16] = secretBytesAgain[i];
        }
        return Keys.hmacShaKeyFor(secretBytes256);
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
