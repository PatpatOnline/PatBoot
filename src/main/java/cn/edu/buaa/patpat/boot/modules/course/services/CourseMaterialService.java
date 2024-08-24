package cn.edu.buaa.patpat.boot.modules.course.services;

import cn.edu.buaa.patpat.boot.common.Globals;
import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.common.utils.Medias;
import cn.edu.buaa.patpat.boot.exceptions.ForbiddenException;
import cn.edu.buaa.patpat.boot.exceptions.InternalServerErrorException;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import cn.edu.buaa.patpat.boot.modules.course.dto.CourseMaterialDto;
import cn.edu.buaa.patpat.boot.modules.course.dto.CreateCourseMaterialRequest;
import cn.edu.buaa.patpat.boot.modules.course.dto.UpdateCourseMaterialRequest;
import cn.edu.buaa.patpat.boot.modules.course.models.entities.CourseMaterial;
import cn.edu.buaa.patpat.boot.modules.course.models.mappers.CourseMaterialMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseMaterialService extends BaseService {
    private final CourseMaterialMapper courseMaterialMapper;
    private final BucketApi bucketApi;

    public CourseMaterial upload(CreateCourseMaterialRequest request, MultipartFile file) {
        if (courseMaterialMapper.existsByCourseIdAndFilename(request.getCourseId(), request.getFilename())) {
            throw new ForbiddenException(M("course.material.duplicate"));
        }
        save(request.getCourseId(), request.getFilename(), file);

        CourseMaterial material = mappers.map(request, CourseMaterial.class);
        courseMaterialMapper.save(material);

        return material;
    }

    /**
     * Only modify the file when request.getFilename() is not null.
     * request.getFilename() is consistent with file.getOriginalFilename().
     */
    public CourseMaterial update(int id, int courseId, UpdateCourseMaterialRequest request, MultipartFile file) {
        CourseMaterial material = courseMaterialMapper.find(id, courseId);
        if (material == null) {
            throw new NotFoundException(M("course.material.exists.not"));
        }

        if (request.getFilename() != null) {
            if (courseMaterialMapper.existsByCourseIdAndFilename(courseId, request.getFilename())) {
                throw new ForbiddenException(M("course.material.duplicate"));
            }
            delete(courseId, material.getFilename());
            save(courseId, request.getFilename(), file);
        }
        mappers.map(request, material);
        material.update();
        courseMaterialMapper.update(material);

        return material;
    }

    public void delete(int id, int courseId) {
        int updated = courseMaterialMapper.delete(id, courseId);
        if (updated == 0) {
            throw new NotFoundException(M("course.material.exists.not"));
        }
    }

    public List<CourseMaterialDto> query(int courseId) {
        var materials = courseMaterialMapper.query(courseId);
        var list = materials.stream().map(m -> mappers.map(m, CourseMaterialDto.class)).toList();
        list.forEach(m -> m.initUrl(bucketApi));
        return list;
    }

    private void delete(int courseId, String filename) {
        String record = bucketApi.toRecord(Globals.COURSE_TAG, String.valueOf(courseId), filename);
        String path = bucketApi.recordToPublicPath(record);
        Medias.removeSilently(path);
    }

    private void save(int courseId, String filename, MultipartFile file) {
        String record = bucketApi.toRecord(Globals.COURSE_TAG, String.valueOf(courseId), filename);
        String path = bucketApi.recordToPublicPath(record);
        try {
            Medias.save(path, file);
        } catch (IOException e) {
            log.error("Failed to save course material", e);
            throw new InternalServerErrorException(M("course.material.save.error"));
        }
    }
}
