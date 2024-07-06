package cn.edu.buaa.patpat.boot.modules.stream.config;

import cn.edu.buaa.patpat.boot.modules.stream.dto.WebSocketPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class MessageDispatcher {
    private final Map<WebSocketSession, String> sessions = new ConcurrentHashMap<>();

    void addSession(String tag, WebSocketSession session) {
        sessions.put(session, tag);
    }

    /**
     * Broadcast message to all connected sessions.
     *
     * @param payload Message payload.
     */
    public void broadcast(WebSocketPayload<?> payload) {
        List<WebSocketSession> invalidSessions = new ArrayList<>();
        for (WebSocketSession session : sessions.keySet()) {
            try {
                session.sendMessage(payload.toTextMessage());
            } catch (Exception e) {
                log.error("Failed to broadcast message to {}", sessions.get(session));
                invalidSessions.add(session);
            }
        }
        invalidSessions.forEach(this::removeSession);
    }

    String removeSession(WebSocketSession session) {
        return sessions.remove(session);
    }

    /**
     * Broadcast a message to all connected sessions with specific tags.
     *
     * @param tags    Session tags.
     * @param payload Message payload.
     */
    public void broadcast(Collection<String> tags, WebSocketPayload<?> payload) {
        tags.forEach(tag -> send(tag, payload));
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
        List<WebSocketSession> invalidSessions = new ArrayList<>();
        for (Map.Entry<WebSocketSession, String> entry : sessions.entrySet()) {
            if (entry.getValue().equals(tag)) {
                try {
                    entry.getKey().sendMessage(payload.toTextMessage());
                } catch (Exception e) {
                    log.error("Failed to send message to tag: {}", tag);
                    invalidSessions.add(entry.getKey());
                }
            }
        }
        invalidSessions.forEach(this::removeSession);
    }
}
