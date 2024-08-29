/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.group.controllers;

import cn.edu.buaa.patpat.boot.aspect.ValidateMultipartFile;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.ResourceResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.group.aspect.ValidateGroup;
import cn.edu.buaa.patpat.boot.modules.group.dto.GroupAssignmentDto;
import cn.edu.buaa.patpat.boot.modules.group.dto.GroupScoreStudentDto;
import cn.edu.buaa.patpat.boot.modules.group.dto.SubmitGroupAssignmentRequest;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.Group;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupAssignment;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupMember;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupScore;
import cn.edu.buaa.patpat.boot.modules.group.services.GroupAssignmentService;
import cn.edu.buaa.patpat.boot.modules.group.services.impl.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/group/assignment")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Group Assignment", description = "Group Assignment API")
public class GroupAssignmentController extends BaseController {
    private final GroupAssignmentService groupAssignmentService;
    private final GroupService groupService;

    @GetMapping("")
    @Operation(summary = "Get group assignment", description = "Get group assignment of the current course")
    @ValidateCourse
    @ValidatePermission
    public DataResponse<GroupAssignmentDto> detail(
            @CourseId Integer courseId,
            AuthPayload auth
    ) {
        GroupAssignment assignment = groupAssignmentService.find(courseId);
        if ((assignment == null) || (!assignment.isVisible() && auth.isStudent())) {
            return DataResponse.ok(null);
        }
        return DataResponse.ok(mappers.map(assignment, GroupAssignmentDto.class));
    }

    @PostMapping("submit")
    @Operation(summary = "Submit group assignment", description = "Submit group assignment of the current course")
    @ValidateMultipartFile(extensions = { "zip" }, maxSize = 64)
    @ValidateCourse
    @ValidatePermission
    @ValidateGroup(requireInGroup = true, requireOwner = true)
    public DataResponse<GroupScoreStudentDto> submit(
            @RequestParam MultipartFile file,
            @CourseId Integer courseId,
            AuthPayload auth,
            Group group
    ) {
        var members = groupService.getMembersInGroup(group.getId());
        var request = new SubmitGroupAssignmentRequest(courseId, group, members, file);
        GroupScore score = groupAssignmentService.submit(request);
        return DataResponse.ok(
                M("group.assignment.submit.success"),
                mappers.map(score, GroupScoreStudentDto.class));
    }

    @GetMapping("download")
    @Operation(summary = "Download group assignment", description = "Download group assignment of the current course")
    @ValidateCourse
    @ValidatePermission
    @ValidateGroup(requireInGroup = true)
    public ResponseEntity<Resource> download(
            @CourseId Integer courseId,
            AuthPayload auth,
            GroupMember member
    ) {
        log.info("{} initiated download of group assignment for group {}", auth.getName(), member.getGroupId());
        Resource resource = groupAssignmentService.download(courseId, member.getGroupId(), false);
        return ResourceResponse.ok(resource);
    }
}
