package cn.edu.buaa.patpat.boot.modules.course.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCourseRequest {
    @Size(min = 1, max = 255)
    private String name;

    @Size(min = 1, max = 15)
    private String code;

    @Size(min = 1, max = 15)
    private String semester;

    private boolean active;
}
