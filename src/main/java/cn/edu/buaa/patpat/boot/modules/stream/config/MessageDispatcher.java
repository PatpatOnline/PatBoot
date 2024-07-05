package cn.edu.buaa.patpat.boot.modules.stream.config;

import cn.edu.buaa.patpat.boot.modules.stream.dto.WebSocketPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class MessageDispatcher {
    private final Map<WebSocketSession, String> sessions = new ConcurrentHashMap<>();

    void addSession(String tag, WebSocketSession session) {
        sessions.put(session, tag);
    }

    String removeSession(WebSocketSession session) {
        return sessions.remove(session);
    }

    /**
     * Send a message to a specific session by tag.
     * Send a message to a specific session by tag.
     *
     * @param tag     Session tag.
     * @param payload Message payload.
     */
    @Async
    public void send(String tag, WebSocketPayload<?> payload) {
        try {
            for (Map.Entry<WebSocketSession, String> entry : sessions.entrySet()) {
                if (entry.getValue().equals(tag)) {
                    entry.getKey().sendMessage(payload.toTextMessage());
                }
            }
        } catch (Exception e) {
            log.error("Failed to send message to tag: {}", tag, e);
        }
    }

    /**
     * Broadcast message to all connected sessions.
     *
     * @param payload Message payload.
     */
    public void broadcast(WebSocketPayload<?> payload) {
        try {
            for (WebSocketSession session : sessions.keySet()) {
                session.sendMessage(payload.toTextMessage());
            }
        } catch (Exception e) {
            log.error("Failed to broadcast message", e);
        }
    }

    /**
     * Broadcast a message to all connected sessions with specific tags.
     *
     * @param tags    Session tags.
     * @param payload Message payload.
     */
    public void broadcast(Collection<String> tags, WebSocketPayload<?> payload) {
        try {
            for (Map.Entry<WebSocketSession, String> entry : sessions.entrySet()) {
                if (tags.contains(entry.getValue())) {
                    entry.getKey().sendMessage(payload.toTextMessage());
                }
            }
        } catch (Exception e) {
            log.error("Failed to broadcast message", e);
        }
    }
}
