/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.stream.api;

import cn.edu.buaa.patpat.boot.modules.stream.config.MessageDispatcher;
import cn.edu.buaa.patpat.boot.modules.stream.dto.WebSocketPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class StreamApi {
    private final MessageDispatcher dispatcher;

    @Async
    public void broadcast(WebSocketPayload<?> payload) {
        dispatcher.broadcast(payload);
    }

    @Async
    public void broadcast(Collection<String> tags, WebSocketPayload<?> payload) {
        dispatcher.broadcast(tags, payload);
    }

    @Async
    public void send(String tag, WebSocketPayload<?> payload) {
        dispatcher.send(tag, payload);
    }
}
