package cn.edu.buaa.patpat.boot.modules.stream.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.socket.TextMessage;

import static cn.edu.buaa.patpat.boot.config.Globals.JSON_MAPPER;

@Data
@AllArgsConstructor
public class WebSocketPayload<TData> {
    private String type;
    private TData data;

    public static WebSocketPayload<String> message(String message) {
        return of("message", message);
    }

    public static <TData> WebSocketPayload<TData> of(String type, TData data) {
        return new WebSocketPayload<>(type, data);
    }

    public TextMessage toTextMessage() {
        return new TextMessage(toString());
    }

    @Override
    public String toString() {
        try {
            return JSON_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Invalid WebSocketPayload";
        }
    }
}
