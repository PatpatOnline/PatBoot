/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.group.controllers;

import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.exceptions.ForbiddenException;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.group.aspect.ValidateGroup;
import cn.edu.buaa.patpat.boot.modules.group.aspect.WithGroupConfig;
import cn.edu.buaa.patpat.boot.modules.group.dto.CreateGroupRequest;
import cn.edu.buaa.patpat.boot.modules.group.dto.GroupDto;
import cn.edu.buaa.patpat.boot.modules.group.dto.UpdateGroupRequest;
import cn.edu.buaa.patpat.boot.modules.group.dto.UpdateWeightRequest;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.Group;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupConfig;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupMember;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupScoreListStudentView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupScoreListView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupWithScoreView;
import cn.edu.buaa.patpat.boot.modules.group.services.impl.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/group")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Group", description = "Group API")
public class GroupController extends BaseController {
    private final GroupService groupService;

    @GetMapping("config")
    @Operation(summary = "Get group configuration", description = "Get group configuration of the current course")
    @ValidateCourse
    @WithGroupConfig
    public DataResponse<GroupConfig> getConfig(
            @CourseId Integer courseId,
            GroupConfig config
    ) {
        return DataResponse.ok(config);
    }

    @PostMapping("create")
    @Operation(summary = "Student create a group", description = "Student create a group when they are not in any group")
    @ValidateCourse
    @ValidatePermission
    @WithGroupConfig(requireEnabled = true)
    @ValidateGroup(requireNotInGroup = true)
    public DataResponse<GroupDto> create(
            @RequestBody @Valid CreateGroupRequest request,
            @CourseId Integer courseId,
            AuthPayload auth
    ) {
        Group group = groupService.create(courseId, auth.getId(), request);
        return DataResponse.ok(
                M("group.create.success"),
                mappers.map(group, GroupDto.class));
    }

    @RequestMapping(value = "update", method = { RequestMethod.POST, RequestMethod.PUT })
    @Operation(summary = "Student update their group", description = "Student update their group when they are the owner of the group")
    @ValidateCourse
    @ValidatePermission
    @ValidateGroup(requireInGroup = true, requireOwner = true)
    public DataResponse<GroupDto> update(
            @RequestBody @Valid UpdateGroupRequest request,
            @CourseId Integer courseId,
            AuthPayload auth,
            Group group
    ) {
        Group updated = groupService.update(group, request);
        return DataResponse.ok(
                M("group.update.success"),
                mappers.map(updated, GroupDto.class));
    }

    @RequestMapping(value = "dismiss", method = { RequestMethod.POST, RequestMethod.DELETE })
    @Operation(summary = "Student dismiss their group", description = "Student dismiss a group when they are the owner of the group")
    @ValidateCourse
    @ValidatePermission
    @WithGroupConfig(requireEnabled = true)
    @ValidateGroup(requireInGroup = true, requireOwner = true)
    public MessageResponse dismiss(
            @CourseId Integer courseId,
            AuthPayload auth,
            GroupMember member
    ) {
        groupService.dismiss(member.getGroupId(), courseId);
        return MessageResponse.ok(M("group.delete.success"));
    }

    @GetMapping("")
    @Operation(summary = "Group member get their group", description = "Get the group and all members")
    @ValidateCourse
    @ValidatePermission
    @WithGroupConfig
    @ValidateGroup
    public DataResponse<GroupWithScoreView> detail(
            @CourseId Integer courseId,
            AuthPayload auth,
            GroupConfig config,
            GroupMember member
    ) {
        if (member == null) {
            return DataResponse.ok(null);
        }

        GroupView group = groupService.getGroup(member.getGroupId(), config);
        GroupScoreListView score = groupService.findScore(member.getGroupId());
        return DataResponse.ok(new GroupWithScoreView(
                group,
                (score == null) ? null : mappers.map(score, GroupScoreListStudentView.class)));
    }

    @PostMapping("join/{id}")
    @Operation(summary = "Student joins a group", description = "Student joins a group when they are not in any group")
    @ValidateCourse
    @ValidatePermission
    @WithGroupConfig(requireEnabled = true)
    @ValidateGroup(requireNotInGroup = true)
    public DataResponse<GroupView> join(
            @PathVariable int id,
            @CourseId Integer courseId,
            AuthPayload auth,
            GroupConfig config
    ) {
        groupService.join(id, courseId, auth.getId(), config);
        GroupView group = groupService.getGroup(id, config);
        return DataResponse.ok(M("group.join.success"), group);
    }

    @PostMapping("quit")
    @Operation(summary = "Student quit a group", description = "Student quit their group")
    @ValidateCourse
    @ValidatePermission
    @WithGroupConfig(requireEnabled = true)
    @ValidateGroup(requireInGroup = true)
    public MessageResponse quit(
            @CourseId Integer courseId,
            AuthPayload auth,
            Group group,
            GroupMember member
    ) {
        if (member.isOwner()) {
            throw new ForbiddenException(M("group.quit.owner"));
        }
        if (group.isLocked()) {
            throw new ForbiddenException(M("group.locked"));
        }
        groupService.quit(courseId, auth.getId());
        return MessageResponse.ok(M("group.quit.success"));
    }

    @PostMapping("kick/{accountId}")
    @Operation(summary = "Group owner kicks a member", description = "Owner of the group kicks a member")
    @ValidateCourse
    @ValidatePermission
    @WithGroupConfig(requireEnabled = true)
    @ValidateGroup(requireInGroup = true, requireOwner = true)
    public MessageResponse kick(
            @PathVariable int accountId,
            @CourseId Integer courseId,
            AuthPayload auth,
            Group group
    ) {
        if (accountId == auth.getId()) {
            throw new ForbiddenException(M("group.kick.self"));
        }
        if (group.isLocked()) {
            throw new ForbiddenException(M("group.locked"));
        }
        groupService.kick(courseId, group.getId(), accountId);
        return MessageResponse.ok(M("group.kick.success"));
    }

    @GetMapping("query")
    @Operation(summary = "Query groups", description = "Query all groups in the current course")
    @ValidateCourse
    @WithGroupConfig
    public DataResponse<List<GroupView>> query(
            @CourseId Integer courseId,
            GroupConfig config
    ) {
        List<GroupView> groups = groupService.queryGroups(courseId, config);
        return DataResponse.ok(groups);
    }

    @RequestMapping(value = "weight/{id}", method = { RequestMethod.POST, RequestMethod.PUT })
    @Operation(summary = "Group owner update member weight", description = "Group owner update member weight")
    @ValidateCourse
    @ValidatePermission
    @WithGroupConfig
    @ValidateGroup(requireInGroup = true, requireOwner = true)
    public DataResponse<Integer> weight(
            @PathVariable int id,
            @RequestBody @Valid UpdateWeightRequest request,
            @CourseId Integer courseId,
            AuthPayload auth,
            GroupConfig config,
            GroupMember member
    ) {
        var weight = request.getWeight();
        if (weight < config.getMinWeight()) {
            weight = config.getMinWeight();
        } else if (weight > config.getMaxWeight()) {
            weight = config.getMaxWeight();
        }
        groupService.updateWeight(courseId, member.getGroupId(), id, weight);
        return DataResponse.ok(weight);
    }
}
