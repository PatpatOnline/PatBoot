/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.group.controllers;

import cn.edu.buaa.patpat.boot.aspect.ValidateParameters;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.dto.ResourceResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.group.dto.CreateGroupAssignmentRequest;
import cn.edu.buaa.patpat.boot.modules.group.dto.GroupAssignmentDto;
import cn.edu.buaa.patpat.boot.modules.group.dto.UpdateGroupAssignmentRequest;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupAssignment;
import cn.edu.buaa.patpat.boot.modules.group.services.GroupAssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/admin/group/assignment")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Group Assignment Admin", description = "Group Assignment Admin API")
public class GroupAssignmentAdminController extends BaseController {
    private final GroupAssignmentService groupAssignmentService;

    @PostMapping("create")
    @Operation(summary = "Create group assignment", description = "Create group assignment for the current course")
    @ValidateParameters
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse(allowRoot = false)
    public DataResponse<GroupAssignmentDto> create(
            @RequestBody @Valid CreateGroupAssignmentRequest request,
            @CourseId Integer courseId
    ) {
        GroupAssignment assignment = groupAssignmentService.create(courseId, request);
        return DataResponse.ok(
                M("group.assignment.create.success"),
                mappers.map(assignment, GroupAssignmentDto.class));
    }

    @PutMapping("update")
    @Operation(summary = "Update group assignment", description = "Update group assignment for the current course")
    @ValidateParameters
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse(allowRoot = false)
    public DataResponse<GroupAssignmentDto> update(
            @RequestBody @Valid UpdateGroupAssignmentRequest request,
            @CourseId Integer courseId
    ) {
        GroupAssignment assignment = groupAssignmentService.update(courseId, request);
        return DataResponse.ok(
                M("group.assignment.update.success"),
                mappers.map(assignment, GroupAssignmentDto.class));
    }

    @DeleteMapping("delete")
    @Operation(summary = "Delete group assignment", description = "Delete group assignment for the current course")
    @ValidateParameters
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse(allowRoot = false)
    public MessageResponse delete(
            @CourseId Integer courseId
    ) {
        groupAssignmentService.delete(courseId);
        return MessageResponse.ok(M("group.assignment.delete.success"));
    }

    @GetMapping("download/{groupId}")
    @Operation(summary = "Download a specific group assignment", description = "Download a specific group assignment for the current course")
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    public ResponseEntity<Resource> download(
            @PathVariable int groupId,
            @CourseId Integer courseId,
            AuthPayload auth
    ) {
        log.warn("{} initiates download of group assignment {} in course {}", auth.getName(), groupId, courseId);
        Resource resource = groupAssignmentService.download(courseId, groupId, true);
        String filename = groupAssignmentService.getArtifactName(groupId);
        return ResourceResponse.ok(resource, filename);
    }


    @GetMapping("download")
    @Operation(summary = "Download all group assignments", description = "Download all group assignments of the current course")
    @ValidateCourse
    @ValidatePermission(AuthLevel.TA)
    public ResponseEntity<Resource> downloadAll(
            @CourseId Integer courseId,
            AuthPayload auth
    ) {
        log.warn("{} initiates download of all group assignments in course {}", auth.getName(), courseId);
        var download = groupAssignmentService.downloadAll(courseId);
        return ResourceResponse.ok(download.getFirst(), download.getSecond());
    }
}
