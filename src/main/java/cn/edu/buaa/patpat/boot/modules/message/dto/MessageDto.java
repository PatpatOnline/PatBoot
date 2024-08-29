/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.message.dto;

import cn.edu.buaa.patpat.boot.common.models.HasCreated;
import cn.edu.buaa.patpat.boot.extensions.mappers.Mappers;
import cn.edu.buaa.patpat.boot.modules.message.models.entities.Message;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MessageDto extends HasCreated {
    private int id;
    private int type;

    private int courseId;
    private int accountId;

    @JsonIgnore
    private String content;
    @JsonProperty("content")
    private Object contentObj;

    @JsonIgnore
    private String argument;
    @JsonProperty("argument")
    private Object argumentObj;

    private boolean read;

    public static MessageDto of(Message message, Mappers mappers) {
        MessageDto dto = mappers.map(message, MessageDto.class);
        try {
            if (message.getContent() != null) {
                dto.setContentObj(mappers.fromJson(message.getContent()));
            }
            if (message.getArgument() != null) {
                dto.setArgumentObj(mappers.fromJson(message.getArgument()));
            }
        } catch (JsonProcessingException e) {
            // ignore
        }
        return dto;
    }
}
