/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.problem.controllers;

import cn.edu.buaa.patpat.boot.aspect.ValidateMultipartFile;
import cn.edu.buaa.patpat.boot.aspect.ValidatePagination;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.dto.PageListDto;
import cn.edu.buaa.patpat.boot.common.dto.ResourceResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.extensions.validation.Validator;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.problem.dto.*;
import cn.edu.buaa.patpat.boot.modules.problem.models.entities.Problem;
import cn.edu.buaa.patpat.boot.modules.problem.models.mappers.ProblemFilter;
import cn.edu.buaa.patpat.boot.modules.problem.models.views.ProblemListView;
import cn.edu.buaa.patpat.boot.modules.problem.models.views.ProblemSelectView;
import cn.edu.buaa.patpat.boot.modules.problem.services.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/admin/problem")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Problem Admin", description = "Admin problem management API")
public class ProblemAdminController extends BaseController {
    private final ProblemService problemService;

    @PostMapping("create")
    @Operation(summary = "Create a new problem", description = "T.A. creates a new problem")
    @ValidateMultipartFile(extensions = { "zip" })
    @ValidatePermission(AuthLevel.TA)
    public DataResponse<CreateProblemResponse> create(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam boolean hidden,
            @RequestParam MultipartFile file
    ) {
        var request = new CreateProblemRequest(title, description, hidden, file);
        Validator.validate(request);

        Problem problem = problemService.create(request);
        var response = CreateProblemResponse.of(problem, mappers);

        return DataResponse.ok(M("problem.create.success"), response);
    }

    @PostMapping("update/{id}")
    @Operation(summary = "Update problem info", description = "T.A. updates problem info")
    @ValidateMultipartFile(allowNull = true, extensions = { "zip" })
    @ValidatePermission(AuthLevel.TA)
    public DataResponse<UpdateProblemResponse> update(
            @PathVariable int id,
            @RequestParam @Nullable String title,
            @RequestParam @Nullable String description,
            @RequestParam @Nullable Boolean hidden,
            @RequestParam @Nullable MultipartFile file
    ) {
        var request = new UpdateProblemRequest(title, description, hidden, file);
        Problem problem = problemService.update(id, request);
        var response = UpdateProblemResponse.of(problem, mappers);
        return DataResponse.ok(M("problem.update.success"), response);
    }

    @PostMapping("delete/{id}")
    @Operation(summary = "Delete a problem", description = "T.A. deletes a problem")
    @ValidatePermission(AuthLevel.TA)
    public MessageResponse delete(
            @PathVariable int id
    ) {
        problemService.delete(id);
        return MessageResponse.ok(M("problem.delete.success"));
    }

    @GetMapping("download/{id}")
    @Operation(summary = "Download a problem configuration", description = "Download a problem configuration")
    @ValidatePermission(AuthLevel.TA)
    public ResponseEntity<Resource> download(
            @PathVariable int id
    ) {
        Resource resource = problemService.download(id);
        return ResourceResponse.ok(resource, String.format("problem-%d.zip", id));
    }

    @GetMapping("select")
    @Operation(summary = "Get all problems", description = "Get all problems for selection")
    @ValidatePermission(AuthLevel.TA)
    public DataResponse<List<ProblemSelectView>> getAll() {
        List<ProblemSelectView> problems = problemService.getAll();
        return DataResponse.ok(problems);
    }

    @GetMapping("query")
    @Operation(summary = "Query problems", description = "Query problems")
    @ValidatePermission(AuthLevel.TA)
    @ValidatePagination
    public DataResponse<PageListDto<ProblemListView>> query(
            @RequestParam(name = "p") int page,
            @RequestParam(name = "ps") int pageSize,
            @RequestParam @Nullable String title,
            @RequestParam @Nullable Boolean hidden
    ) {
        var filter = new ProblemFilter(title, hidden);
        var problems = problemService.query(page, pageSize, filter);
        return DataResponse.ok(problems);
    }

    @GetMapping("query/{id}")
    @Operation(summary = "Query a problem", description = "Query a problem by id")
    @ValidatePermission(AuthLevel.TA)
    public DataResponse<ProblemDto> queryById(
            @PathVariable int id
    ) {
        var problem = problemService.query(id, true);
        return DataResponse.ok(problem);
    }
}
