package cn.edu.buaa.patpat.boot.modules.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoursePayload {
    private Integer courseId;
    private Integer studentId;
    private Integer teacherId;
}
