/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.account.controllers;

import cn.edu.buaa.patpat.boot.aspect.Page;
import cn.edu.buaa.patpat.boot.aspect.PageSize;
import cn.edu.buaa.patpat.boot.aspect.ValidatePagination;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.dto.PageListDto;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.exceptions.ForbiddenException;
import cn.edu.buaa.patpat.boot.modules.account.dto.AccountDto;
import cn.edu.buaa.patpat.boot.modules.account.dto.CreateAccountRequest;
import cn.edu.buaa.patpat.boot.modules.account.dto.RegisterRequest;
import cn.edu.buaa.patpat.boot.modules.account.dto.UpdateAccountRequest;
import cn.edu.buaa.patpat.boot.modules.account.models.entities.Account;
import cn.edu.buaa.patpat.boot.modules.account.models.mappers.AccountFilter;
import cn.edu.buaa.patpat.boot.modules.account.models.views.AccountDetailView;
import cn.edu.buaa.patpat.boot.modules.account.models.views.AccountListView;
import cn.edu.buaa.patpat.boot.modules.account.services.AccountService;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @ValidatePermission(AuthLevel.TEACHER)
    public DataResponse<AccountDto> create(
            @RequestBody @Valid CreateAccountRequest request
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

    @RequestMapping(value = "password/reset/{id}", method = { RequestMethod.POST, RequestMethod.PUT })
    @Operation(summary = "Reset password", description = "Reset any account's password")
    @ValidatePermission(AuthLevel.TA)
    public MessageResponse resetPassword(
            @PathVariable int id
    ) {
        accountService.resetPassword(id);
        return MessageResponse.ok(M("account.password.reset.success"));
    }

    @RequestMapping(value = "update/{id}", method = { RequestMethod.POST, RequestMethod.PUT })
    @Operation(summary = "Update an account and role", description = "T.A. updates an account")
    @ValidatePermission(AuthLevel.TA)
    public DataResponse<AccountDto> update(
            @PathVariable int id,
            @RequestBody @Valid UpdateAccountRequest request,
            AuthPayload auth
    ) {
        if (id == 1) {
            throw new ForbiddenException(M("account.update.forbidden"));
        } else if (id == auth.getId()) {
            throw new ForbiddenException(M("account.update.forbidden.self"));
        }

        AccountDto dto = accountService.update(id, request);
        return DataResponse.ok(M("account.update.success"), dto);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get account detail", description = "Get detail of an account")
    @ValidatePermission(AuthLevel.TA)
    public DataResponse<AccountDetailView> detail(
            @PathVariable int id
    ) {
        var view = accountService.findDetailView(id);
        return DataResponse.ok(view);
    }

    @GetMapping("query")
    @Operation(summary = "Query accounts", description = "Query all accounts")
    @ValidatePagination
    @ValidatePermission(AuthLevel.TA)
    public DataResponse<PageListDto<AccountListView>> query(
            @RequestParam(name = "p") @Page int page,
            @RequestParam(name = "ps") @PageSize int pageSize,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String buaaId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer role
    ) {
        if ((role != null) && (role < 0 || role > 2)) {
            throw new BadRequestException(M("account.role.invalid"));
        }
        AccountFilter filter = new AccountFilter(id, buaaId, name, role);
        var pageList = accountService.query(page, pageSize, filter);
        return DataResponse.ok(pageList);
    }
}
