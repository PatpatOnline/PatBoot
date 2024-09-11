/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.group.controllers;

import cn.edu.buaa.patpat.boot.aspect.ValidateParameters;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.ResourceResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.group.aspect.WithGroupConfig;
import cn.edu.buaa.patpat.boot.modules.group.dto.ScoreGroupRequest;
import cn.edu.buaa.patpat.boot.modules.group.dto.UpdateGroupConfigRequest;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupConfig;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupScore;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupScoreListView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.RogueStudentView;
import cn.edu.buaa.patpat.boot.modules.group.services.GroupConfigService;
import cn.edu.buaa.patpat.boot.modules.group.services.impl.GroupAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/group")
@RequiredArgsConstructor
@Tag(name = "Group Admin", description = "Group Admin API")
public class GroupAdminController extends BaseController {
    private final GroupConfigService groupConfigService;
    private final GroupAdminService groupAdminService;

    @PostMapping("config/update")
    @Operation(summary = "Update group configuration", description = "Update group configuration of the current course")
    @ValidateParameters
    @ValidateCourse
    @ValidatePermission(AuthLevel.TA)
    @WithGroupConfig
    public DataResponse<GroupConfig> updateConfig(
            @RequestBody @Valid UpdateGroupConfigRequest request,
            @CourseId Integer courseId,
            GroupConfig config
    ) {
        config = groupConfigService.update(config, request);
        return DataResponse.ok(config);
    }

    @GetMapping("query/rogue")
    @Operation(summary = "Query rogue students", description = "Get all rogue students (not in any group) of the current course")
    @ValidateCourse
    @ValidatePermission(AuthLevel.TA)
    public DataResponse<List<RogueStudentView>> queryRogueStudents(
            @CourseId Integer courseId
    ) {
        List<RogueStudentView> rogueStudents = groupAdminService.queryRogueStudents(courseId);
        return DataResponse.ok(rogueStudents);
    }

    @PostMapping("score/{groupId}")
    @Operation(summary = "Score group", description = "Score a group of the current course")
    @ValidatePermission(AuthLevel.TA)
    public DataResponse<GroupScoreListView> score(
            @PathVariable Integer groupId,
            @RequestBody @Valid ScoreGroupRequest request
    ) {
        GroupScore score = groupAdminService.score(groupId, request.getScore());
        return DataResponse.ok(mappers.map(score, GroupScoreListView.class));
    }

    @GetMapping("query/scores")
    @Operation(summary = "Query group scores", description = "Get all group scores of the current course")
    @ValidateCourse
    @ValidatePermission(AuthLevel.TA)
    public DataResponse<List<GroupScoreListView>> queryScores(
            @CourseId Integer courseId
    ) {
        List<GroupScoreListView> scores = groupAdminService.queryScores(courseId);
        return DataResponse.ok(scores);
    }

    @GetMapping("export")
    @Operation(summary = "Export groups", description = "Export all groups of the current course")
    @ValidateCourse
    @ValidatePermission(AuthLevel.TA)
    @WithGroupConfig
    public ResponseEntity<Resource> export(
            @CourseId Integer courseId,
            GroupConfig config
    ) {
        Resource resource = groupAdminService.exportGroups(courseId, config);
        return ResourceResponse.ok(resource);
    }
}
