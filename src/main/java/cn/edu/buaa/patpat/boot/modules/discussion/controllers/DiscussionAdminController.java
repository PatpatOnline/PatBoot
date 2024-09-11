/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.discussion.controllers;

import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.discussion.services.DiscussionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/admin/discussion")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Discussion Admin", description = "Discussion Admin API")
public class DiscussionAdminController extends BaseController {
    private final DiscussionService discussionService;

    @PostMapping("top/{id}")
    @Operation(summary = "Top/Untop a discussion", description = "Top/Untop a discussion to the top of the list")
    @ValidatePermission(AuthLevel.TA)
    public MessageResponse top(
            @PathVariable int id,
            @RequestParam boolean topped
    ) {
        discussionService.top(id, topped);
        return MessageResponse.ok(topped
                ? M("discussion.top.success")
                : M("discussion.untop.success"));
    }

    @PostMapping("star/{id}")
    @Operation(summary = "Star/Unstar a discussion", description = "Star/Unstar a discussion")
    @ValidatePermission(AuthLevel.TA)
    public MessageResponse star(
            @PathVariable int id,
            @RequestParam boolean starred
    ) {
        discussionService.star(id, starred);
        return MessageResponse.ok(starred
                ? M("discussion.star.success")
                : M("discussion.unstar.success"));
    }
}
