/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.discussion.controllers;

import cn.edu.buaa.patpat.boot.aspect.Page;
import cn.edu.buaa.patpat.boot.aspect.PageSize;
import cn.edu.buaa.patpat.boot.aspect.ValidatePagination;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.dto.PageListDto;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.common.utils.Strings;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.discussion.dto.CreateDiscussionRequest;
import cn.edu.buaa.patpat.boot.modules.discussion.dto.DiscussionUpdateDto;
import cn.edu.buaa.patpat.boot.modules.discussion.dto.UpdateDiscussionRequest;
import cn.edu.buaa.patpat.boot.modules.discussion.models.entities.Discussion;
import cn.edu.buaa.patpat.boot.modules.discussion.models.entities.Subscription;
import cn.edu.buaa.patpat.boot.modules.discussion.models.mappers.DiscussionFilter;
import cn.edu.buaa.patpat.boot.modules.discussion.models.views.DiscussionView;
import cn.edu.buaa.patpat.boot.modules.discussion.models.views.DiscussionWithReplyView;
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
@RequestMapping("/api/discussion")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Discussion", description = "Discussion API, must select course first")
public class DiscussionController extends BaseController {
    private final DiscussionService discussionService;
    private final ReplyService replyService;
    private final SubscriptionService subscriptionService;

    @PostMapping("create")
    @Operation(summary = "Create a new discussion", description = "Student create a new discussion in a course")
    @ValidatePermission
    @ValidateCourse
    public DataResponse<DiscussionView> create(
            @RequestBody @Valid CreateDiscussionRequest request,
            AuthPayload auth,
            @CourseId Integer courseId
    ) {
        request.setContent(Strings.fromBase64(request.getContent()));

        var discussion = discussionService.create(request, courseId, auth.getId());
        if (discussion == null) {
            throw new BadRequestException(M("discussion.create.error"));
        }
        var view = discussionService.get(courseId, discussion.getId(), auth.getId());

        // The author of the discussion is automatically subscribed to the discussion.
        subscriptionService.subscribe(new Subscription(auth.getId(), discussion.getId(), auth.getBuaaId()));

        log.info("User {} created discussion {}: {}", auth.getBuaaId(), discussion.getId(), discussion.getTitle());

        return DataResponse.ok(M("discussion.create.success"), view);
    }

    @PostMapping("update/{id}")
    @Operation(summary = "Update a discussion", description = "Student update their discussion or T.A. update any discussion")
    @ValidatePermission
    @ValidateCourse
    public DataResponse<DiscussionUpdateDto> update(
            @PathVariable int id,
            @RequestBody @Valid UpdateDiscussionRequest request,
            AuthPayload auth,
            @CourseId Integer courseId
    ) {
        if (request.getContent() != null) {
            request.setContent(Strings.fromBase64(request.getContent()));
        }

        var discussion = discussionService.update(id, request, courseId, auth);

        log.info("User {} updated discussion {}: {}", auth.getBuaaId(), discussion.getId(), discussion.getTitle());

        return DataResponse.ok(M("discussion.update.success"),
                mappers.map(discussion, DiscussionUpdateDto.class));
    }

    @PostMapping("delete/{id}")
    @Operation(summary = "Delete a discussion", description = "Student delete their discussion or T.A. delete any discussion")
    @ValidatePermission
    @ValidateCourse
    public MessageResponse delete(
            @PathVariable int id,
            AuthPayload auth,
            @CourseId Integer courseId
    ) {
        Discussion discussion = discussionService.delete(courseId, id, auth);

        log.info("User {} deleted discussion {}: {}", auth.getBuaaId(), discussion.getId(), discussion.getTitle());

        return MessageResponse.ok(M("discussion.delete.success"));
    }

    @GetMapping("{id}")
    @Operation(summary = "Get a discussion", description = "Get the detail of a discussion")
    @ValidatePermission
    @ValidateCourse
    public DataResponse<DiscussionWithReplyView> detail(
            @PathVariable int id,
            AuthPayload auth,
            @CourseId Integer courseId
    ) {
        var discussion = discussionService.get(courseId, id, auth.getId());
        var replies = replyService.query(discussion.getId(), auth.getId());

        return DataResponse.ok(new DiscussionWithReplyView(discussion, replies));
    }

    @PostMapping("like/{id}")
    @Operation(summary = "Like/Unlike a discussion", description = "Student like/unlike a discussion")
    @ValidatePermission
    @ValidateCourse
    public MessageResponse like(
            @PathVariable int id,
            @RequestParam boolean liked,
            AuthPayload auth,
            @CourseId Integer courseId
    ) {
        if (!discussionService.exists(courseId, id)) {
            throw new NotFoundException(M("discussion.exists.not"));
        }
        discussionService.like(id, auth.getId(), liked);
        return MessageResponse.ok(liked
                ? M("discussion.like.success")
                : M("discussion.unlike.success"));
    }

    @PostMapping("subscribe/{id}")
    @Operation(summary = "Subscribe/Unsubscribe a discussion", description = "Student subscribe/unsubscribe a discussion")
    @ValidatePermission
    @ValidateCourse
    public MessageResponse subscribe(
            @PathVariable int id,
            @RequestParam boolean subscribed,
            AuthPayload auth,
            @CourseId Integer courseId
    ) {
        if (!discussionService.exists(courseId, id)) {
            throw new NotFoundException(M("discussion.exists.not"));
        }
        if (subscribed) {
            subscriptionService.subscribe(new Subscription(auth.getId(), id, auth.getBuaaId()));
        } else {
            subscriptionService.unsubscribe(auth.getId(), id);
        }

        return MessageResponse.ok(subscribed
                ? M("discussion.subscribe.success")
                : M("discussion.unsubscribe.success"));
    }

    @GetMapping("query")
    @Operation(summary = "Query discussions", description = "Query discussions in a course")
    @ValidatePagination
    @ValidatePermission
    @ValidateCourse
    public DataResponse<PageListDto<DiscussionView>> query(
            @RequestParam(value = "p") @Page int page,
            @RequestParam(value = "ps") @PageSize int pageSize,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Integer type,
            @CourseId Integer courseId,
            AuthPayload auth
    ) {
        DiscussionFilter filter = new DiscussionFilter(query, type);
        PageListDto<DiscussionView> dto = discussionService.query(courseId, auth.getId(), page, pageSize, filter);

        return DataResponse.ok(dto);
    }
}
