package cn.edu.buaa.patpat.boot.modules.course.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateCourseMaterialRequest {
    private String filename;

    @Size(max = 255)
    private String comment;
}
