package cn.edu.buaa.patpat.boot.modules.course.models.mappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentFilter {
    private String buaaId;
    private String name;
    private Integer teacherId;
}
