package cn.edu.buaa.patpat.boot.extensions.jwt;

/**
 * Interface for issuing and verifying JWT tokens.
 */
public interface IJwtIssuer {
    /**
     * Issue a JWT token with the given subject.
     *
     * @param subject The subject to be included in the token.
     * @return The issued JWT token.
     */
    String issue(String subject);

    /**
     * Verify the given JWT token.
     *
     * @param token The token to be verified.
     * @return The payload of the token.
     * @throws JwtVerifyException If the token is invalid.
     */
    String verify(String token) throws JwtVerifyException;
}
