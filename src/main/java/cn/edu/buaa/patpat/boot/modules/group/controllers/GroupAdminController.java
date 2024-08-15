package cn.edu.buaa.patpat.boot.modules.group.controllers;

import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.group.aspect.WithGroupConfig;
import cn.edu.buaa.patpat.boot.modules.group.dto.UpdateGroupConfigRequest;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupConfig;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.RogueStudentView;
import cn.edu.buaa.patpat.boot.modules.group.services.GroupConfigService;
import cn.edu.buaa.patpat.boot.modules.group.services.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.service.RequestBodyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/group")
@RequiredArgsConstructor
@Tag(name = "Group Admin", description = "Group Admin API")
public class GroupAdminController extends BaseController {
    private final GroupConfigService groupConfigService;
    private final RequestBodyService requestBodyBuilder;
    private final GroupService groupService;

    @GetMapping("config")
    @Operation(summary = "Get group configuration", description = "Get group configuration of the current course")
    @ValidateCourse
    @ValidatePermission(AuthLevel.TA)
    @WithGroupConfig
    public DataResponse<GroupConfig> detail(
            @CourseId Integer courseId,
            GroupConfig config
    ) {
        return DataResponse.ok(config);
    }

    @PutMapping("config/update")
    @Operation(summary = "Update group configuration", description = "Update group configuration of the current course")
    @ValidateCourse
    @ValidatePermission(AuthLevel.TA)
    @WithGroupConfig
    public DataResponse<GroupConfig> update(
            @RequestBody @Valid UpdateGroupConfigRequest request,
            @CourseId Integer courseId,
            GroupConfig config
    ) {
        config = groupConfigService.update(config, request);
        return DataResponse.ok(config);
    }

    @GetMapping("query")
    @Operation(summary = "Query groups", description = "Get all groups of the current course")
    @ValidateCourse
    @ValidatePermission(AuthLevel.TA)
    @WithGroupConfig
    public DataResponse<List<GroupView>> query(
            @CourseId Integer courseId,
            GroupConfig config
    ) {
        List<GroupView> groups = groupService.queryGroups(courseId, config);
        return DataResponse.ok(groups);
    }

    @GetMapping("query/rogue")
    @Operation(summary = "Query rogue students", description = "Get all rogue students (not in any group) of the current course")
    @ValidateCourse
    @ValidatePermission(AuthLevel.TA)
    public DataResponse<List<RogueStudentView>> queryRogueStudents(
            @CourseId Integer courseId
    ) {
        List<RogueStudentView> rogueStudents = groupService.queryRogueStudents(courseId);
        return DataResponse.ok(rogueStudents);
    }
}
