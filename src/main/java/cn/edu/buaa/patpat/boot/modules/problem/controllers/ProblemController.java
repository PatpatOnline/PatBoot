/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.problem.controllers;

import cn.edu.buaa.patpat.boot.aspect.ValidatePagination;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.PageListDto;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.problem.dto.ProblemDto;
import cn.edu.buaa.patpat.boot.modules.problem.models.mappers.ProblemFilter;
import cn.edu.buaa.patpat.boot.modules.problem.models.views.ProblemListView;
import cn.edu.buaa.patpat.boot.modules.problem.services.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/problem")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Problem", description = "Problem API")
public class ProblemController extends BaseController {
    private final ProblemService problemService;

    @GetMapping("query")
    @Operation(summary = "Query non-hidden problems", description = "Student query non-hidden problems")
    @ValidatePagination
    public DataResponse<PageListDto<ProblemListView>> query(
            @RequestParam(name = "p") int page,
            @RequestParam(name = "ps") int pageSize,
            @RequestParam @Nullable String title
    ) {
        var filter = new ProblemFilter(title, false);
        var problems = problemService.query(page, pageSize, filter);
        return DataResponse.ok(problems);
    }

    @GetMapping("query/{id}")
    @Operation(summary = "Query problem detail", description = "Query detail of non-hidden problem")
    public DataResponse<ProblemDto> query(@PathVariable int id) {
        var problem = problemService.query(id, false);
        return DataResponse.ok(problem);
    }
}
