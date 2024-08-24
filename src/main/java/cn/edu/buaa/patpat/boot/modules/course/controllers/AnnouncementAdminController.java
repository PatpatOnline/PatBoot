package cn.edu.buaa.patpat.boot.modules.course.controllers;

import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.course.dto.CreateAnnouncementRequest;
import cn.edu.buaa.patpat.boot.modules.course.dto.UpdateAnnouncementRequest;
import cn.edu.buaa.patpat.boot.modules.course.models.entities.Announcement;
import cn.edu.buaa.patpat.boot.modules.course.models.views.AnnouncementBriefView;
import cn.edu.buaa.patpat.boot.modules.course.models.views.AnnouncementView;
import cn.edu.buaa.patpat.boot.modules.course.services.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/admin/announcement")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Announcement Admin", description = "Admin announcement management API")
public class AnnouncementAdminController extends BaseController {
    private final AnnouncementService announcementService;

    @PostMapping("create")
    @Operation(summary = "Create an announcement", description = "T.A. creates an announcement")
    @ValidateCourse
    @ValidatePermission(AuthLevel.TA)
    public DataResponse<AnnouncementView> create(
            @RequestBody @Valid CreateAnnouncementRequest request,
            AuthPayload auth,
            @CourseId Integer courseId
    ) {
        Announcement announcement = announcementService.create(courseId, auth.getId(), request);
        AnnouncementView view = announcementService.get(announcement.getId(), courseId);
        return DataResponse.ok(M("announcement.create.success"), view);
    }

    @PutMapping("update/{id}")
    @Operation(summary = "Update an announcement", description = "T.A. updates an announcement")
    @ValidateCourse
    @ValidatePermission(AuthLevel.TA)
    public DataResponse<AnnouncementView> update(
            @PathVariable int id,
            @RequestBody @Valid UpdateAnnouncementRequest request,
            @CourseId Integer courseId
    ) {
        Announcement announcement = announcementService.update(id, courseId, request);
        AnnouncementView view = announcementService.get(announcement.getId(), courseId);
        return DataResponse.ok(M("announcement.update.success"), view);
    }

    @DeleteMapping("delete/{id}")
    @Operation(summary = "Delete an announcement", description = "T.A. deletes an announcement")
    @ValidateCourse(allowRoot = false)
    @ValidatePermission(AuthLevel.TA)
    public MessageResponse delete(@PathVariable int id) {
        announcementService.delete(id);
        return MessageResponse.ok(M("announcement.delete.success"));
    }

    @GetMapping("info/{id}")
    @Operation(summary = "Query an announcement info", description = "Get the brief information of an announcement")
    @ValidateCourse
    public DataResponse<AnnouncementBriefView> info(
            @PathVariable int id,
            @CourseId Integer courseId
    ) {
        AnnouncementBriefView announcement = announcementService.getBrief(id, courseId);
        return DataResponse.ok(announcement);
    }
}
