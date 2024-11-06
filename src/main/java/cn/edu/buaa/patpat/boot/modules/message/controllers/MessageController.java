/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.message.controllers;

import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.message.dto.MessageDto;
import cn.edu.buaa.patpat.boot.modules.message.services.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/message")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Message", description = "Message API")
public class MessageController extends BaseController {
    private final MessageService messageService;

    @GetMapping("query")
    @Operation(summary = "Query messages", description = "Query all messages in the current course")
    @ValidateCourse
    @ValidatePermission
    public DataResponse<List<MessageDto>> query(
            @CourseId Integer courseId,
            AuthPayload auth
    ) {
        List<MessageDto> messages = messageService.query(courseId, auth.getId()).stream().map(
                message -> MessageDto.of(message, mappers)
        ).toList();
        return DataResponse.ok(messages);
    }

    @RequestMapping(value = "read/{id}", method = { RequestMethod.POST, RequestMethod.PUT })
    @Operation(summary = "Read message", description = "Mark a message as read/unread")
    @ValidatePermission
    public DataResponse<Boolean> read(
            @PathVariable int id,
            @RequestParam boolean read,
            AuthPayload auth
    ) {
        messageService.readMessage(id, auth.getId(), read);
        return DataResponse.ok(read
                ? M("message.read.success")
                : M("message.unread.success"), read);
    }

    @RequestMapping(value = "update/{id}", method = { RequestMethod.POST, RequestMethod.PUT })
    @Operation(summary = "Update message", description = "Update the argument of a message")
    @ValidatePermission
    public DataResponse<Object> update(
            @RequestBody Object argument,
            @PathVariable int id,
            AuthPayload auth
    ) {
        messageService.updateMessage(id, auth.getId(), argument);
        return DataResponse.ok(M("message.update.success"), argument);
    }

    @RequestMapping(value = "delete/{id}", method = { RequestMethod.POST, RequestMethod.DELETE })
    @Operation(summary = "Delete message", description = "Delete a message")
    @ValidatePermission
    public MessageResponse delete(
            @PathVariable int id,
            AuthPayload auth
    ) {
        messageService.deleteMessage(id, auth.getId());
        return MessageResponse.ok(M("message.delete.success"));
    }
}
