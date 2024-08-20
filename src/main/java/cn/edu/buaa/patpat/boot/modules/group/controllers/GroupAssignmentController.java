package cn.edu.buaa.patpat.boot.modules.group.controllers;

import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.group.dto.GroupAssignmentDto;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupAssignment;
import cn.edu.buaa.patpat.boot.modules.group.services.GroupAssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/group/assignment")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Group Assignment", description = "Group Assignment API")
public class GroupAssignmentController extends BaseController {
    private final GroupAssignmentService groupAssignmentService;

    @GetMapping("")
    @Operation(summary = "Get group assignment", description = "Get group assignment of the current course")
    @ValidateCourse
    @ValidatePermission
    public DataResponse<GroupAssignmentDto> detail(
            @CourseId Integer courseId,
            AuthPayload auth
    ) {
        GroupAssignment assignment = groupAssignmentService.get(courseId);
        if (!assignment.isVisible() && auth.isStudent()) {
            throw new NotFoundException(M("group.assignment.exists.not"));
        }
        return DataResponse.ok(mappers.map(assignment, GroupAssignmentDto.class));
    }
}
