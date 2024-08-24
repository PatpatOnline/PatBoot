package cn.edu.buaa.patpat.boot.modules.message.dto;

import cn.edu.buaa.patpat.boot.common.utils.Mappers;
import cn.edu.buaa.patpat.boot.modules.message.models.entities.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageWrapper {
    private int courseId;
    private int accountId;
    private MessagePayload<?> payload;

    public static MessageWrapper of(int courseId, int accountId, MessagePayload<?> payload) {
        return new MessageWrapper(courseId, accountId, payload);
    }

    public Message toMessage(Mappers mappers) throws JsonProcessingException {
        return payload.toMessage(courseId, accountId, mappers);
    }
}
