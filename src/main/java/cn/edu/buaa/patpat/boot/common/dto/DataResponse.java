package cn.edu.buaa.patpat.boot.common.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DataResponse<TData> {
    private final int status;
    private final String message;
    private final TData data;

    DataResponse(MessageResponse response) {
        this(response.getStatus(), response.getMessage(), null);
    }

    public static <TData> DataResponse<TData> ok(TData data) {
        return ok(HttpStatus.OK.getReasonPhrase(), data);
    }

    public static <TData> DataResponse<TData> ok(String message, TData data) {
        return build(HttpStatus.OK.value(), message, data);
    }

    private static <Data> DataResponse<Data> build(int status, String message, Data data) {
        return new DataResponse<>(status, message, data);
    }

    public static <TData> DataResponse<TData> error(HttpStatus status, TData data) {
        return error(status, status.getReasonPhrase(), data);
    }

    public static <TData> DataResponse<TData> error(HttpStatus status, String message, TData data) {
        return error(status.value(), message, data);
    }

    public static <TData> DataResponse<TData> error(int status, String message, TData data) {
        return build(status, message, data);
    }

    public static <TData> DataResponse<TData> badRequest(TData data) {
        return badRequest(HttpStatus.BAD_REQUEST.getReasonPhrase(), data);
    }

    public static <TData> DataResponse<TData> badRequest(String message, TData data) {
        return error(HttpStatus.BAD_REQUEST, message, data);
    }

    public static <TData> DataResponse<TData> unauthorized(TData data) {
        return unauthorized(HttpStatus.UNAUTHORIZED.getReasonPhrase(), data);
    }

    public static <TData> DataResponse<TData> unauthorized(String message, TData data) {
        return error(HttpStatus.UNAUTHORIZED, message, data);
    }

    public static <TData> DataResponse<TData> forbidden(TData data) {
        return forbidden(HttpStatus.FORBIDDEN.getReasonPhrase(), data);
    }

    public static <TData> DataResponse<TData> forbidden(String message, TData data) {
        return error(HttpStatus.FORBIDDEN, message, data);
    }

    public static <TData> DataResponse<TData> notFound(TData data) {
        return notFound(HttpStatus.NOT_FOUND.getReasonPhrase(), data);
    }

    public static <TData> DataResponse<TData> notFound(String message, TData data) {
        return error(HttpStatus.NOT_FOUND, message, data);
    }

    public static <TData> DataResponse<TData> internalServerError(TData data) {
        return internalServerError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), data);
    }

    public static <TData> DataResponse<TData> internalServerError(String message, TData data) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, message, data);
    }
}
