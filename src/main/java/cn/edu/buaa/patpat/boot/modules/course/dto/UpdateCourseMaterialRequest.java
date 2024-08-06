package cn.edu.buaa.patpat.boot.modules.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateCourseMaterialRequest {
    private String filename;
    private String comment;
}
