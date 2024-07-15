package cn.edu.buaa.patpat.boot.extensions.jwt;

public class JwtVerifyException extends Exception {
    public JwtVerifyException(String message) {
        super(message);
    }

    public JwtVerifyException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtVerifyException(Throwable cause) {
        super(cause);
    }
}
