/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.course.controllers;

import cn.edu.buaa.patpat.boot.aspect.ValidateMultipartFile;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.extensions.validation.Validator;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.course.dto.CourseMaterialDto;
import cn.edu.buaa.patpat.boot.modules.course.dto.CreateCourseMaterialRequest;
import cn.edu.buaa.patpat.boot.modules.course.dto.UpdateCourseMaterialRequest;
import cn.edu.buaa.patpat.boot.modules.course.models.entities.CourseMaterial;
import cn.edu.buaa.patpat.boot.modules.course.services.CourseMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/admin/course/material")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Course Material Admin", description = "Admin course material management API")
public class CourseMaterialAdminController extends BaseController {
    private final CourseMaterialService courseMaterialService;
    private final BucketApi bucketApi;

    @PostMapping("upload")
    @Operation(summary = "Upload course material", description = "T.A. uploads a new course material")
    @ValidatePermission(AuthLevel.TA)
    @ValidateMultipartFile
    @ValidateCourse
    public DataResponse<CourseMaterialDto> upload(
            @RequestParam MultipartFile file,
            @RequestParam(required = false) String comment,
            @CourseId Integer courseId
    ) {
        var request = new CreateCourseMaterialRequest(courseId, file.getOriginalFilename(), comment);
        Validator.validate(request);

        CourseMaterial material = courseMaterialService.upload(request, file);
        CourseMaterialDto dto = mappers.map(material, CourseMaterialDto.class);
        dto.initUrl(bucketApi);

        return DataResponse.ok(M("course.material.create.success"), dto);
    }

    @PostMapping("update/{id}")
    @Operation(summary = "Update course material", description = "T.A. updates a course material")
    @ValidatePermission(AuthLevel.TA)
    @ValidateMultipartFile(allowNull = true, allowEmpty = true)
    @ValidateCourse
    public DataResponse<CourseMaterialDto> update(
            @PathVariable Integer id,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false) String comment,
            @CourseId Integer courseId
    ) {
        UpdateCourseMaterialRequest request;
        if (file == null || file.isEmpty()) {
            request = new UpdateCourseMaterialRequest(null, comment);
        } else {
            request = new UpdateCourseMaterialRequest(file.getOriginalFilename(), comment);
        }
        CourseMaterial material = courseMaterialService.update(id, courseId, request, file);
        CourseMaterialDto dto = mappers.map(material, CourseMaterialDto.class);
        dto.initUrl(bucketApi);
        return DataResponse.ok(M("course.material.update.success"), dto);
    }

    @RequestMapping(value = "delete/{id}", method = { RequestMethod.POST, RequestMethod.DELETE })
    @Operation(summary = "Delete course material", description = "T.A. deletes a course material")
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    public MessageResponse delete(
            @PathVariable Integer id,
            @CourseId Integer courseId
    ) {
        courseMaterialService.delete(id, courseId);
        return MessageResponse.ok(M("course.material.delete.success"));
    }

    @GetMapping("query")
    @Operation(summary = "Get all course materials", description = "T.A. queries all course materials")
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    public DataResponse<List<CourseMaterialDto>> query(
            @CourseId Integer courseId
    ) {
        var materials = courseMaterialService.query(courseId);
        return DataResponse.ok(materials);
    }
}
