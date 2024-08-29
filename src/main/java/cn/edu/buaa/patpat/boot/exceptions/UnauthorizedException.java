/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Unauthorized Request")
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        this("Unauthorized Request");
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
