package cn.edu.buaa.patpat.boot.modules.stream.config;

import cn.edu.buaa.patpat.boot.extensions.mappers.Mappers;
import cn.edu.buaa.patpat.boot.modules.stream.dto.WebSocketPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageDispatcher {
    private final Map<WebSocketSession, String> sessions = new ConcurrentHashMap<>();
    private final Map<String, Set<WebSocketSession>> tags = new ConcurrentHashMap<>();
    private final Mappers mappers;

    /**
     * Broadcast message to all connected sessions.
     *
     * @param payload Message payload.
     */
    public void broadcast(WebSocketPayload<?> payload) {
        List<WebSocketSession> invalidSessions = new ArrayList<>();
        for (WebSocketSession session : sessions.keySet()) {
            try {
                session.sendMessage(payload.toTextMessage(mappers));
            } catch (Exception e) {
                log.error("Failed to broadcast message to {}", sessions.get(session));
                invalidSessions.add(session);
            }
        }
        invalidSessions.forEach(this::removeSession);
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
        for (var sessions : tags.getOrDefault(tag, Collections.emptySet())) {
            try {
                sessions.sendMessage(payload.toTextMessage(mappers));
            } catch (Exception e) {
                log.error("Failed to send message to tag: {}", tag);
                invalidSessions.add(sessions);
            }
        }
        invalidSessions.forEach(this::removeSession);
    }

    void addSession(String tag, WebSocketSession session) {
        sessions.put(session, tag);
        tags.computeIfAbsent(tag, k -> new HashSet<>()).add(session);
    }

    String removeSession(WebSocketSession session) {
        String tag = sessions.remove(session);
        if (tag != null) {
            tags.computeIfPresent(tag, (k, set) -> {
                set.remove(session);
                return set.isEmpty() ? null : set;
            });
        }
        return tag;
    }
}
