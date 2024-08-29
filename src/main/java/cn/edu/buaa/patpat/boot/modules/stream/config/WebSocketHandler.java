/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.stream.config;

import cn.edu.buaa.patpat.boot.extensions.jwt.JwtVerifyException;
import cn.edu.buaa.patpat.boot.extensions.mappers.Mappers;
import cn.edu.buaa.patpat.boot.modules.auth.api.AuthApi;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.stream.dto.WebSocketPayload;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {
    private final MessageDispatcher dispatcher;
    private final AuthApi authApi;
    private final Mappers mappers;

    /**
     * Handle new WebSocket connection. URL should be in the format of /ws/{jwt}.
     *
     * @param session WebSocket session.
     * @throws Exception Exception.
     */
    @Override
    public void afterConnectionEstablished(@Nonnull WebSocketSession session) throws Exception {
        URI uri = session.getUri();
        if (uri == null) {
            log.error("Request uri is null!");
            session.sendMessage(WebSocketPayload.message("Invalid request").toTextMessage(mappers));
            session.close();
            return;
        }

        // check if path is /ws
        if (!"/ws".equals(uri.getPath())) {
            log.error("Invalid path: {}", uri.getPath());
            session.sendMessage(WebSocketPayload.message("Invalid path").toTextMessage(mappers));
            session.close();
            return;
        }

        // validate JWT in query
        String jwt = getJwtFromQuery(uri);
        if (jwt == null) {
            log.error("Invalid JWT in query");
            session.sendMessage(WebSocketPayload.message("Invalid JWT").toTextMessage(mappers));
            session.close();
            return;
        }

        AuthPayload payload;
        try {
            payload = authApi.verifyJwt(jwt);
        } catch (JwtVerifyException e) {
            log.error("Expired JWT: {}", e.getMessage());
            session.sendMessage(WebSocketPayload.message("Expired JWT").toTextMessage(mappers));
            session.close();
            return;
        }

        dispatcher.addSession(payload.getBuaaId(), session);
        dispatcher.send(payload.getBuaaId(), WebSocketPayload.message("Battle control online"));

        log.info("New WebSocket connection with {}", payload.getBuaaId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, @Nonnull TextMessage message) throws IOException {
        session.sendMessage(WebSocketPayload.message("You are the silent role.").toTextMessage(mappers));
    }

    @Override
    public void handleTransportError(WebSocketSession session, @Nonnull Throwable throwable) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        String tag = dispatcher.removeSession(session);
        log.error("WebSocket connection with {} encountered error: {}", tag, throwable.toString());
    }

    @Override
    public void afterConnectionClosed(@Nonnull WebSocketSession session, @Nonnull CloseStatus status) {
        String tag = dispatcher.removeSession(session);
        // only log successful close
        if (tag != null) {
            log.info("WebSocket connection with {} closed", tag);
        }
    }

    private String getJwtFromQuery(URI uri) {
        String queries = uri.getQuery();
        if (queries == null) {
            return null;
        }

        String[] queryParts = queries.split("&");
        if (queryParts.length != 1) {
            return null;
        }
        String query = queryParts[0];
        if (!query.startsWith("jwt=")) {
            return null;
        }
        return query.substring(4);
    }
}
