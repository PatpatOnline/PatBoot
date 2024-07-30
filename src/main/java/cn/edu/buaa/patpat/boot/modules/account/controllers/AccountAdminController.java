package cn.edu.buaa.patpat.boot.modules.account.controllers;

import cn.edu.buaa.patpat.boot.aspect.ValidateParameters;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.account.dto.AccountDto;
import cn.edu.buaa.patpat.boot.modules.account.dto.CreateAccountRequest;
import cn.edu.buaa.patpat.boot.modules.account.dto.RegisterRequest;
import cn.edu.buaa.patpat.boot.modules.account.models.entities.Account;
import cn.edu.buaa.patpat.boot.modules.account.services.AccountService;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/admin/account")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Account Admin", description = "Admin account management API")
public class AccountAdminController extends BaseController {
    private final AccountService accountService;
    private final BucketApi bucketApi;

    @PostMapping("create")
    @Operation(summary = "Create an account", description = "Teacher creates an account")
    @ValidateParameters
    @ValidatePermission(AuthLevel.TEACHER)
    public DataResponse<AccountDto> create(
            @RequestBody @Valid CreateAccountRequest request,
            BindingResult bindingResult
    ) {
        var registerRequest = mappers.map(request, RegisterRequest.class);
        if (registerRequest.isTeacher()) {
            registerRequest.setTa(true);
        }
        registerRequest.setPassword(registerRequest.getBuaaId());

        Account account = accountService.register(registerRequest);
        AccountDto dto = mappers.map(account, AccountDto.class);
        dto.setAvatar(bucketApi.recordToUrl(dto.getAvatar()));

        return DataResponse.ok(
                M("account.create.success"),
                dto);
    }

    @PostMapping("password/reset/{id}")
    @Operation(summary = "Reset password", description = "Reset any account's password")
    @ValidatePermission(AuthLevel.TA)
    public MessageResponse resetPassword(
            @PathVariable int id
    ) {
        accountService.resetPassword(id);
        return MessageResponse.ok(M("account.password.reset.success"));
    }
}
