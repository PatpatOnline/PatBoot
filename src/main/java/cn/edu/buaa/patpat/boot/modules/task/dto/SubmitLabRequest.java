package cn.edu.buaa.patpat.boot.modules.task.dto;

import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.dto.CoursePayload;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class SubmitLabRequest {
    private int labId;
    private AuthPayload auth;
    private CoursePayload course;

    private MultipartFile file;
}
