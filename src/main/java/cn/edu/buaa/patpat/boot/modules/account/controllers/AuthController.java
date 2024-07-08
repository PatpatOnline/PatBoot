package cn.edu.buaa.patpat.boot.modules.account.controllers;

import cn.edu.buaa.patpat.boot.modules.account.dto.AuthLevel;
import cn.edu.buaa.patpat.boot.annotations.RequestValidation;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.exceptions.InternalServerErrorException;
import cn.edu.buaa.patpat.boot.extensions.jwt.JwtIssueException;
import cn.edu.buaa.patpat.boot.modules.account.dto.AccountDto;
import cn.edu.buaa.patpat.boot.modules.account.dto.LoginRequest;
import cn.edu.buaa.patpat.boot.modules.account.dto.RegisterRequest;
import cn.edu.buaa.patpat.boot.modules.account.services.AccountService;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import io.swagger.v3.oas.annotations.Operation;
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

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController extends BaseController {
    private final AccountService accountService;

    @PostMapping("register")
    @Operation(summary = "Register a new account", description = "Register a new account, only available in development")
    @RequestValidation
    public MessageResponse register(
            @RequestBody @Valid RegisterRequest dto,
            BindingResult bindingResult
    ) {
        accountService.register(dto);
        return MessageResponse.ok("Register success");
    }

    @PostMapping("login")
    @Operation(summary = "Login", description = "Login with BUAA ID and password")
    @RequestValidation
    public DataResponse<AccountDto> login(
            @RequestBody @Valid LoginRequest dto,
            BindingResult bindingResult,
            HttpServletResponse response
    ) {
        var account = accountService.login(dto);
        AuthPayload auth = objects.map(account, AuthPayload.class);
        try {
            String jwt = authApi.issueJwt(auth);
            String refresh = authApi.issueRefresh(auth);
            response.addCookie(authApi.setJwtCookie(jwt));
            response.addCookie(authApi.setRefreshCookie(refresh));
        } catch (JwtIssueException e) {
            throw new InternalServerErrorException("Failed to issue JWT");
        }

        return DataResponse.ok(account);
    }

    @PostMapping("logout")
    @Operation(summary = "Logout", description = "Logout and clear the session")
    @RequestValidation(authLevel = AuthLevel.LOGIN)
    public MessageResponse logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        response.addCookie(authApi.cleanJwtCookie());
        response.addCookie(authApi.cleanRefreshCookie());
        return MessageResponse.ok("Logout successfully");
    }
}
