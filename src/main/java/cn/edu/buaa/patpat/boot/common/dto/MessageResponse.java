package cn.edu.buaa.patpat.boot.common.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageResponse {
    private final int status;
    private final String message;

    public static MessageResponse ok() {
        return ok(HttpStatus.OK.getReasonPhrase());
    }

    public static MessageResponse ok(String message) {
        return build(HttpStatus.OK.value(), message);
    }

    private static MessageResponse build(int status, String message) {
        return new MessageResponse(status, message);
    }

    public static MessageResponse error(HttpStatus status) {
        return error(status, status.getReasonPhrase());
    }

    public static MessageResponse error(HttpStatus status, String message) {
        return error(status.value(), message);
    }

    public static MessageResponse error(int status, String message) {
        return build(status, message);
    }

    public static MessageResponse badRequest() {
        return badRequest(HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    public static MessageResponse badRequest(String message) {
        return error(HttpStatus.BAD_REQUEST, message);
    }

    public static MessageResponse unauthorized() {
        return unauthorized(HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    public static MessageResponse unauthorized(String message) {
        return error(HttpStatus.UNAUTHORIZED, message);
    }

    public static MessageResponse forbidden() {
        return forbidden(HttpStatus.FORBIDDEN.getReasonPhrase());
    }

    public static MessageResponse forbidden(String message) {
        return error(HttpStatus.FORBIDDEN, message);
    }

    public static MessageResponse methodNotAllowed() {
        return methodNotAllowed(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
    }

    public static MessageResponse methodNotAllowed(String message) {
        return error(HttpStatus.METHOD_NOT_ALLOWED, message);
    }

    public static MessageResponse notFound() {
        return notFound(HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    public static MessageResponse notFound(String message) {
        return error(HttpStatus.NOT_FOUND, message);
    }

    public static MessageResponse internalServerError(String message) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public <TData> DataResponse<TData> toDataResponse() {
        return new DataResponse<>(this);
    }
}
