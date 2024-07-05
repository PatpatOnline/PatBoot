package cn.edu.buaa.patpat.boot.modules.stream.config;

import cn.edu.buaa.patpat.boot.modules.stream.dto.WebSocketPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {
    private final MessageDispatcher dispatcher;


    /**
     * Handle new WebSocket connection. URL should be in the format of /ws/{jwt}.
     *
     * @param session WebSocket session.
     * @throws Exception Exception.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        URI uri = session.getUri();
        if (uri == null) {
            session.close();
            log.error("Request uri is null!");
            return;
        }

        int pos = uri.toString().lastIndexOf('/');
        if (pos == -1) {
            session.close();
            log.error("Invalid uri: {}", uri);
            return;
        }

        String jwt = uri.toString().substring(pos + 1);
        dispatcher.addSession(jwt, session);
        session.sendMessage(WebSocketPayload.message("Battle control online").toTextMessage());

        log.info("New connection established: {}", jwt);
    }
}
