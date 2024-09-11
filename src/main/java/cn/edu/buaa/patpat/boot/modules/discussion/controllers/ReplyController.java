/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.discussion.controllers;

import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.discussion.dto.CreateReplyRequest;
import cn.edu.buaa.patpat.boot.modules.discussion.dto.ReplyUpdateDto;
import cn.edu.buaa.patpat.boot.modules.discussion.dto.UpdateReplyRequest;
import cn.edu.buaa.patpat.boot.modules.discussion.models.entities.Reply;
import cn.edu.buaa.patpat.boot.modules.discussion.models.views.ReplyView;
import cn.edu.buaa.patpat.boot.modules.discussion.services.DiscussionService;
import cn.edu.buaa.patpat.boot.modules.discussion.services.ReplyService;
import cn.edu.buaa.patpat.boot.modules.discussion.services.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/discussion/reply")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reply", description = "Reply API")
public class ReplyController extends BaseController {
    private final ReplyService replyService;
    private final DiscussionService discussionService;
    private final SubscriptionService subscriptionService;

    @PostMapping("create")
    @Operation(summary = "Create a new reply", description = "Create a new reply in a discussion")
    @ValidateCourse
    @ValidatePermission
    public DataResponse<ReplyView> create(
            @RequestBody @Valid CreateReplyRequest request,
            AuthPayload auth,
            @CourseId Integer courseId
    ) {
        if (!discussionService.exists(courseId, request.getDiscussionId())) {
            throw new NotFoundException(M("discussion.exists.not"));
        }

        Reply reply = replyService.create(request, auth.getId());
        if (reply == null) {
            throw new NotFoundException(M("reply.create.error"));
        }
        ReplyView view = replyService.get(reply.getId(), auth.getId());

        subscriptionService.notifySubscribers(courseId, request.getDiscussionId(), reply.getId(), auth);

        return DataResponse.ok(M("reply.create.success"), view);
    }

    @PostMapping("update/{id}")
    @Operation(summary = "Update a reply", description = "Student update their reply in a discussion, T.A. can update any reply")
    @ValidatePermission
    @ValidateCourse
    public DataResponse<ReplyUpdateDto> update(
            @PathVariable int id,
            @RequestBody @Valid UpdateReplyRequest request,
            AuthPayload auth,
            @CourseId Integer courseId
    ) {
        Reply reply = replyService.update(id, request, courseId, auth);

        return DataResponse.ok(
                M("reply.update.success"),
                mappers.map(reply, ReplyUpdateDto.class));
    }

    @PostMapping("delete/{id}")
    @Operation(summary = "Delete a reply", description = "Student delete their reply in a discussion, T.A. can delete any reply")
    @ValidatePermission
    @ValidateCourse
    public MessageResponse delete(
            @PathVariable int id,
            AuthPayload auth,
            @CourseId Integer courseId
    ) {
        replyService.delete(id, courseId, auth);

        return MessageResponse.ok(M("reply.delete.success"));
    }

    @PostMapping("like/{id}")
    @Operation(summary = "Like a reply", description = "Like a reply in a discussion")
    @ValidatePermission
    @ValidateCourse
    public MessageResponse like(
            @PathVariable int id,
            @RequestParam boolean liked,
            AuthPayload auth,
            @CourseId Integer courseId
    ) {
        replyService.like(id, courseId, auth.getId(), liked);
        return MessageResponse.ok(liked
                ? M("reply.like.success")
                : M("reply.unlike.success"));
    }
}
