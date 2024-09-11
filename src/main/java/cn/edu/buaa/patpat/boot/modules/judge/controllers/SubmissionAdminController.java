/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.judge.controllers;

import cn.edu.buaa.patpat.boot.aspect.Page;
import cn.edu.buaa.patpat.boot.aspect.PageSize;
import cn.edu.buaa.patpat.boot.aspect.ValidatePagination;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.PageListDto;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.judge.dto.SubmissionAdminDto;
import cn.edu.buaa.patpat.boot.modules.judge.models.mappers.SubmissionFilter;
import cn.edu.buaa.patpat.boot.modules.judge.models.views.SubmissionListView;
import cn.edu.buaa.patpat.boot.modules.judge.services.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin/submission")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Submission Admin", description = "Submission Admin API")
public class SubmissionAdminController extends BaseController {

    private final SubmissionService submissionService;

    @GetMapping("query")
    @Operation(summary = "Query all submissions", description = "Query all submissions in the current course")
    @ValidatePagination
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    public DataResponse<PageListDto<SubmissionListView>> query(
            @RequestParam(name = "p") @Page int page,
            @RequestParam(name = "ps") @PageSize int pageSize,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String buaaId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer problemId,
            @RequestParam(required = false) Integer minScore,
            @RequestParam(required = false) Integer maxScore,
            @CourseId Integer courseId
    ) {
        var filter = new SubmissionFilter(id, buaaId, name, problemId, minScore, maxScore);
        var submissions = submissionService.query(courseId, page, pageSize, filter);
        return DataResponse.ok(submissions);
    }

    @GetMapping("query/{id}")
    @Operation(summary = "Query a submission", description = "Query a submission detail by id")
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    public DataResponse<SubmissionAdminDto> queryById(
            @PathVariable Integer id,
            @CourseId Integer courseId
    ) {
        var view = submissionService.query(id, courseId);
        return DataResponse.ok(view);
    }
}
