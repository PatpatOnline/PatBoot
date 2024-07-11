package cn.edu.buaa.patpat.boot.modules.stream.controllers;

import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.exceptions.UnauthorizedException;
import cn.edu.buaa.patpat.boot.modules.auth.api.AuthApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/stream")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Stream", description = "WebSocket stream API")
public class StreamController {
    private final AuthApi authApi;
    @Value("${config.websocket}")
    private String webSocketUrl;

    @GetMapping("websocket")
    @Operation(summary = "Get WebSocket URL", description = "Get WebSocket URL for the current user")
    public DataResponse<String> getWebSocketUrl(
            HttpServletRequest servletRequest
    ) {
        String jwt = authApi.getJwt(servletRequest);
        if (jwt == null) {
            throw new UnauthorizedException(M("auth.permission.jwt.missing"));
        }
        return DataResponse.ok(webSocketUrl + "/ws?jwt=" + jwt);
    }
}
