package cn.edu.buaa.patpat.boot.modules.course.models.views;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentDetailView extends StudentView {
    private String major;
    private String className;
    private boolean repeat;

    private String avatar;
}
