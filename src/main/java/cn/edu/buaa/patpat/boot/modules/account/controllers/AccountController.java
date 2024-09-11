/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.account.controllers;

import cn.edu.buaa.patpat.boot.aspect.ValidateMultipartFile;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.account.dto.UpdatePasswordRequest;
import cn.edu.buaa.patpat.boot.modules.account.models.views.AccountDetailView;
import cn.edu.buaa.patpat.boot.modules.account.models.views.TeacherIndexView;
import cn.edu.buaa.patpat.boot.modules.account.services.AccountService;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/account")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Account", description = "Account management API")
public class AccountController extends BaseController {
    private final AccountService accountService;

    @PostMapping("password")
    @Operation(summary = "Update password", description = "Update password of the current account")
    @ValidatePermission
    public MessageResponse updatePassword(
            @RequestBody @Valid UpdatePasswordRequest request,
            AuthPayload auth
    ) {
        accountService.updatePassword(auth.getId(), request.getOldPassword(), request.getNewPassword());
        return MessageResponse.ok(M("account.password.update.success"));
    }

    @PostMapping("avatar")
    @Operation(summary = "Update avatar", description = "Update avatar of the current account")
    @ValidateMultipartFile(maxSize = 10, extensions = { "jpg", "jpeg", "png", "svg", "gif" })
    @ValidatePermission
    public DataResponse<String> updateAvatar(
            @RequestParam MultipartFile file,
            AuthPayload auth
    ) {
        var url = accountService.updateAvatar(auth.getId(), auth.getBuaaId(), file);
        return DataResponse.ok(url);
    }

    @GetMapping("self")
    @Operation(summary = "Get account detail", description = "Get detail of the current account")
    @ValidatePermission
    public DataResponse<AccountDetailView> detail(AuthPayload auth) {
        var view = accountService.findDetailView(auth.getId());
        return DataResponse.ok(view);
    }

    @GetMapping("teachers")
    @Operation(summary = "Query teachers", description = "Get all teachers")
    public DataResponse<List<TeacherIndexView>> queryTeachers() {
        return DataResponse.ok(accountService.queryTeachers());
    }
}
