/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.message.api;

import cn.edu.buaa.patpat.boot.modules.message.dto.MessagePayload;
import cn.edu.buaa.patpat.boot.modules.message.dto.MessageWrapper;
import cn.edu.buaa.patpat.boot.modules.message.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageApi {
    private final MessageService messageService;

    @Async
    public void send(int courseId, int accountId, MessagePayload<?> payload) {
        send(MessageWrapper.of(courseId, accountId, payload));
    }

    @Async
    public void broadcast(List<MessageWrapper> wrappers) {
        messageService.sendMessages(wrappers);
    }

    private void send(MessageWrapper wrapper) {
        messageService.sendMessage(wrapper);
    }
}
