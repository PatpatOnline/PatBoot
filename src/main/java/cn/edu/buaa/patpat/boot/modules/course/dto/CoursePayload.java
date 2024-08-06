package cn.edu.buaa.patpat.boot.modules.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoursePayload {
    private int courseId;
    private int studentId;
    private int teacherId;
}
