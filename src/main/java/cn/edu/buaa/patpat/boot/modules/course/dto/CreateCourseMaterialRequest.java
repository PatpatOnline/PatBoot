package cn.edu.buaa.patpat.boot.modules.course.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCourseMaterialRequest {
    @NotNull
    @Min(1)
    private Integer courseId;

    @NotNull
    private String filename;

    @Size(max = 255)
    private String comment;
}
