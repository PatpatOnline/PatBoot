/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.course.controllers;

import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.course.models.views.AnnouncementView;
import cn.edu.buaa.patpat.boot.modules.course.services.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/announcement")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Announcement", description = "Announcement management API")
public class AnnouncementController extends BaseController {
    private final AnnouncementService announcementService;

    @GetMapping("query")
    @Operation(summary = "Query announcements", description = "Get all announcements in the current course")
    @ValidateCourse
    public DataResponse<List<AnnouncementView>> query(
            @CourseId Integer courseId
    ) {
        var announcements = announcementService.getAll(courseId);
        return DataResponse.ok(announcements);
    }

    @GetMapping("query/{id}")
    @Operation(summary = "Query an announcement", description = "Get an announcement by id")
    @ValidateCourse
    public DataResponse<AnnouncementView> query(
            @PathVariable int id,
            @CourseId Integer courseId
    ) {
        var announcement = announcementService.get(id, courseId);
        return DataResponse.ok(announcement);
    }
}
