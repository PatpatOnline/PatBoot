package cn.edu.buaa.patpat.boot.modules.problem.controllers;

import cn.edu.buaa.patpat.boot.aspect.ValidateMultipartFile;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.problem.dto.CreateProblemRequest;
import cn.edu.buaa.patpat.boot.modules.problem.dto.CreateProblemResponse;
import cn.edu.buaa.patpat.boot.modules.problem.models.entities.Problem;
import cn.edu.buaa.patpat.boot.modules.problem.services.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
            @RequestParam MultipartFile file,
            HttpServletRequest servletRequest
    ) {
        var request = new CreateProblemRequest(title, description, hidden, file);
        request.validate();

        Problem problem = problemService.createProblem(request);
        var response = CreateProblemResponse.of(problem, mappers);

        return DataResponse.ok(M("problem.create.success"), response);
    }
}
