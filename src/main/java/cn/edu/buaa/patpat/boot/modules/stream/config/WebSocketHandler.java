package cn.edu.buaa.patpat.boot.modules.stream.config;

import cn.edu.buaa.patpat.boot.extensions.jwt.JwtVerifyException;
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
            session.close();
            log.error("Request uri is null!");
            return;
        }

        // check if path is /ws
        if (!"/ws".equals(uri.getPath())) {
            session.close();
            log.error("Invalid path: {}", uri.getPath());
            return;
        }

        // validate JWT in query
        String jwt = getJwtFromQuery(uri);
        AuthPayload payload;
        try {
            payload = authApi.verifyJwt(jwt);
        } catch (JwtVerifyException e) {
            session.close();
            log.error("Expired JWT: {}", e.getMessage());
            return;
        }

        dispatcher.addSession(payload.getBuaaId(), session);
        dispatcher.send(payload.getBuaaId(), WebSocketPayload.message("Battle control online"));

        log.info("New WebSocket connection with {}", payload.getBuaaId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, @Nonnull TextMessage message) throws IOException {
        session.sendMessage(WebSocketPayload.message("You are the silent role.").toTextMessage());
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
    public void afterConnectionClosed(@Nonnull WebSocketSession session, @Nonnull CloseStatus status) throws Exception {
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
