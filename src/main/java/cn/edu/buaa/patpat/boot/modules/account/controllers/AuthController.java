package cn.edu.buaa.patpat.boot.modules.account.controllers;

import cn.edu.buaa.patpat.boot.aspect.ValidateParameters;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.exceptions.ForbiddenException;
import cn.edu.buaa.patpat.boot.exceptions.InternalServerErrorException;
import cn.edu.buaa.patpat.boot.extensions.jwt.JwtIssueException;
import cn.edu.buaa.patpat.boot.modules.account.dto.LoginRequest;
import cn.edu.buaa.patpat.boot.modules.account.dto.LoginResponse;
import cn.edu.buaa.patpat.boot.modules.account.dto.RegisterRequest;
import cn.edu.buaa.patpat.boot.modules.account.services.AccountService;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.api.CourseApi;
import cn.edu.buaa.patpat.boot.modules.course.models.entities.Course;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController extends BaseController {
    private final AccountService accountService;
    private final CourseApi courseApi;

    @PostMapping("register")
    @Operation(summary = "Register a new account", description = "Register a new account, only available in development")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Register successfully"),
            @ApiResponse(responseCode = "400", description = "BUAA ID already exists")
    })
    @ValidateParameters
    public MessageResponse register(
            @RequestBody @Valid RegisterRequest dto,
            BindingResult bindingResult
    ) {
        accountService.register(dto);
        return MessageResponse.ok("Register success");
    }

    @PostMapping("login")
    @Operation(summary = "Login", description = "Login with BUAA ID and password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successfully"),
            @ApiResponse(responseCode = "400", description = "Account not found or password incorrect"),
            @ApiResponse(responseCode = "403", description = "You are not in any course")
    })
    @ValidateParameters
    public DataResponse<LoginResponse> login(
            @RequestBody @Valid LoginRequest dto,
            BindingResult bindingResult,
            HttpServletResponse servletResponse
    ) {
        var account = accountService.login(dto);
        AuthPayload auth = objects.map(account, AuthPayload.class);
        List<Course> courses = courseApi.getAllAvailableCourses(auth);
        if (courses.isEmpty()) {
            throw new ForbiddenException("You are not in any course");
        }

        try {
            String jwt = authApi.issueJwt(auth);
            String refresh = authApi.issueRefresh(auth);
            servletResponse.addCookie(authApi.setJwtCookie(jwt));
            servletResponse.addCookie(authApi.setRefreshCookie(refresh));
        } catch (JwtIssueException e) {
            throw new InternalServerErrorException("Failed to issue JWT");
        }

        if (courses.size() == 1) {
            // If only one course available, set it as the active course.
            servletResponse.addCookie(courseApi.setCourseCookie(courses.get(0).getId()));
        } else {
            // If more than one course available, force user to select course.
            servletResponse.addCookie(courseApi.cleanCourseCookie());
        }

        return DataResponse.ok(new LoginResponse(account, courses));
    }

    @PostMapping("logout")
    @Operation(summary = "Logout", description = "Logout and clear the session")
    @ValidateParameters
    @ValidatePermission
    public MessageResponse logout(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ) {
        servletResponse.addCookie(authApi.cleanJwtCookie());
        servletResponse.addCookie(authApi.cleanRefreshCookie());
        servletResponse.addCookie(courseApi.cleanCourseCookie());
        return MessageResponse.ok("Logout successfully");
    }
}
