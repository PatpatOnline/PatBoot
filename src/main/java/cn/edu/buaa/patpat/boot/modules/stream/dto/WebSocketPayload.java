/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.stream.dto;

import cn.edu.buaa.patpat.boot.extensions.mappers.Mappers;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.socket.TextMessage;

@Data
@AllArgsConstructor
public class WebSocketPayload<TData> {
    private String type;
    private TData data;

    public static WebSocketPayload<String> message(String message) {
        return of("ws", message);
    }

    public static <TData> WebSocketPayload<TData> of(String type, TData data) {
        return new WebSocketPayload<>(type, data);
    }

    public TextMessage toTextMessage(Mappers mappers) {
        return new TextMessage(mappers.toJson(this, "Invalid message"));
    }
}
