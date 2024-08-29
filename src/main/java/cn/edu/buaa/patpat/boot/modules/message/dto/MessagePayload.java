/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.message.dto;

import cn.edu.buaa.patpat.boot.extensions.mappers.Mappers;
import cn.edu.buaa.patpat.boot.modules.message.models.entities.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessagePayload<TData> {
    private int type;
    private TData data;
    private Object argument;

    public static <TData> MessagePayload<TData> of(int type, TData data) {
        return new MessagePayload<>(type, data, null);
    }

    public static <TData> MessagePayload<TData> of(int type, TData data, Object argument) {
        return new MessagePayload<>(type, data, argument);
    }

    public Message toMessage(int courseId, int accountId, Mappers mappers) throws JsonProcessingException {
        Message message = new Message();

        message.setType(type);
        message.setCourseId(courseId);
        message.setAccountId(accountId);
        message.setContent(mappers.toJson(data));
        if (argument != null) {
            message.setArgument(mappers.toJson(argument));
        }
        message.setRead(false);

        return message;
    }
}
