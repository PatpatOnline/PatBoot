package cn.edu.buaa.patpat.boot.exceptions;

public class TimeoutException extends Exception {
    public TimeoutException(long timeout) {
        this("Timeout after " + timeout + "ms");
    }

    public TimeoutException(String message) {
        super(message);
    }
}
