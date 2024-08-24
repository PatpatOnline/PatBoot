package cn.edu.buaa.patpat.boot.modules.task.dto;

import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.dto.CoursePayload;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DownloadTaskRequest {
    private int id;
    private AuthPayload auth;
    private CoursePayload course;
}
