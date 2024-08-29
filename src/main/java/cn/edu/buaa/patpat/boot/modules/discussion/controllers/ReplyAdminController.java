/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.discussion.controllers;

import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.discussion.services.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/admin/reply")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reply Admin", description = "Reply Admin API")
public class ReplyAdminController extends BaseController {
    private final ReplyService replyService;

    @PutMapping("/verify/{id}")
    @Operation(summary = "Verify/Unverify a reply", description = "Verify/Unverify a reply")
    @ValidatePermission(AuthLevel.TA)
    public MessageResponse verify(
            @PathVariable int id,
            @RequestParam boolean verified
    ) {
        replyService.verify(id, verified);
        return MessageResponse.ok(verified
                ? M("reply.verify.success")
                : M("reply.unverify.success"));
    }
}
