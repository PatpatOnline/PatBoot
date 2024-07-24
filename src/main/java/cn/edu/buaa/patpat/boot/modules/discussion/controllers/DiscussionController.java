package cn.edu.buaa.patpat.boot.modules.discussion.controllers;

import cn.edu.buaa.patpat.boot.aspect.Page;
import cn.edu.buaa.patpat.boot.aspect.PageSize;
import cn.edu.buaa.patpat.boot.aspect.ValidatePagination;
import cn.edu.buaa.patpat.boot.aspect.ValidateParameters;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.dto.PageListDto;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
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
import cn.edu.buaa.patpat.boot.modules.discussion.models.views.DiscussionView;
import cn.edu.buaa.patpat.boot.modules.discussion.models.views.DiscussionWithReplyView;
import cn.edu.buaa.patpat.boot.modules.discussion.services.DiscussionService;
import cn.edu.buaa.patpat.boot.modules.discussion.services.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
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

    @PostMapping("create")
    @Operation(summary = "Create a new discussion", description = "Student create a new discussion in a course")
    @ValidateParameters
    @ValidatePermission
    @ValidateCourse
    public DataResponse<DiscussionView> create(
            @RequestBody @Valid CreateDiscussionRequest request,
            BindingResult bindingResult,
            AuthPayload auth,
            @CourseId Integer courseId
    ) {
        var discussion = discussionService.create(request, courseId, auth.getId());
        if (discussion == null) {
            throw new BadRequestException(M("discussion.create.error"));
        }
        var view = discussionService.detail(courseId, discussion.getId(), auth.getId());

        log.info("User {} created discussion {}: {}", auth.getBuaaId(), discussion.getId(), discussion.getTitle());

        return DataResponse.ok(M("discussion.create.success"), view);
    }

    @PutMapping("update/{id}")
    @Operation(summary = "Update a discussion", description = "Student update their discussion or T.A. update any discussion")
    @ValidateParameters
    @ValidatePermission
    @ValidateCourse
    public DataResponse<DiscussionUpdateDto> update(
            @PathVariable int id,
            @RequestBody @Valid UpdateDiscussionRequest request,
            BindingResult bindingResult,
            AuthPayload auth,
            @CourseId Integer courseId
    ) {
        var discussion = discussionService.update(id, request, courseId, auth);

        log.info("User {} updated discussion {}: {}", auth.getBuaaId(), discussion.getId(), discussion.getTitle());

        return DataResponse.ok(M("discussion.update.success"),
                mappers.map(discussion, DiscussionUpdateDto.class));
    }

    @DeleteMapping("delete/{id}")
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
        var discussion = discussionService.detail(courseId, id, auth.getId());
        var replies = replyService.getAllInDiscussion(discussion.getId(), auth.getId());

        return DataResponse.ok(new DiscussionWithReplyView(discussion, replies));
    }

    @PutMapping("like/{id}")
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

    @GetMapping("all")
    @Operation(summary = "Get all discussions", description = "Get all discussions in a course")
    @ValidatePagination
    @ValidatePermission
    @ValidateCourse
    public DataResponse<PageListDto<DiscussionView>> getAll(
            @RequestParam(value = "p") @Page int page,
            @RequestParam(value = "ps") @PageSize int pageSize,
            AuthPayload auth,
            @CourseId Integer courseId
    ) {
        PageListDto<DiscussionView> dto = discussionService.getAll(courseId, auth.getId(), page, pageSize);

        return DataResponse.ok(dto);
    }
}
