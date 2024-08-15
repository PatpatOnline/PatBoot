package cn.edu.buaa.patpat.boot.modules.course.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateStudentRequest {
    @Min(1)
    private Integer teacherId;

    @Size(min = 1, max = 63)
    private String major;

    @Size(min = 1, max = 15)
    private String className;

    private Boolean repeat;
}
