package cn.edu.buaa.patpat.boot.modules.discussion.controllers;

import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.discussion.dto.CreateReplyRequest;
import cn.edu.buaa.patpat.boot.modules.discussion.models.entities.Reply;
import cn.edu.buaa.patpat.boot.modules.discussion.models.views.ReplyView;
import cn.edu.buaa.patpat.boot.modules.discussion.services.DiscussionService;
import cn.edu.buaa.patpat.boot.modules.discussion.services.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/discussion/reply")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reply", description = "Reply API")
public class ReplyController extends BaseController {
    private final ReplyService replyService;
    private final DiscussionService discussionService;

    @PostMapping("create")
    @Operation(summary = "Create a new reply", description = "Create a new reply in a discussion")
    @ValidateCourse
    @ValidatePermission
    public DataResponse<ReplyView> create(
            @RequestBody @Valid CreateReplyRequest request,
            BindingResult bindingResult,
            AuthPayload auth,
            @CourseId Integer courseId,
            HttpServletRequest servletRequest
    ) {
        if (!discussionService.exists(courseId, request.getDiscussionId())) {
            throw new NotFoundException(M("discussion.exists.not"));
        }
        if (request.getParentId() != 0) {
            if (!replyService.exists(request.getDiscussionId(), request.getParentId())) {
                throw new NotFoundException(M("reply.exists.not"));
            }
        }

        Reply reply = replyService.create(request, auth.getId());
        ReplyView view = replyService.detail(reply.getId(), auth.getId());

        return DataResponse.ok(view);
    }
}
