package cn.edu.buaa.patpat.boot.modules.account.controllers;

import cn.edu.buaa.patpat.boot.aspect.ValidateParameters;
import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.account.dto.UpdatePasswordRequest;
import cn.edu.buaa.patpat.boot.modules.account.services.AccountService;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/account")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Account", description = "Account management API")
public class AccountController extends BaseController {
    private final AccountService accountService;

    @PutMapping("password")
    @Operation(summary = "Change password", description = "Change password")
    @ValidateParameters
    @ValidatePermission
    public MessageResponse updatePassword(
            @RequestBody @Valid UpdatePasswordRequest request,
            BindingResult bindingResult,
            AuthPayload auth
    ) {
        accountService.updatePassword(auth.getId(), request.getOldPassword(), request.getNewPassword());
        return MessageResponse.ok(M("account.password.update.success"));
    }
}
