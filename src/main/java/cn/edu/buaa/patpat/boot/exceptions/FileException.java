package cn.edu.buaa.patpat.boot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "I/O Exception")
public class FileException extends RuntimeException {
    public FileException() {
        this("I/O Exception occurred");
    }

    public FileException(String message) {
        super(message);
    }
}
