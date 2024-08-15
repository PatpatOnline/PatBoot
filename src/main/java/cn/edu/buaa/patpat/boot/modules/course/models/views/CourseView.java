package cn.edu.buaa.patpat.boot.modules.course.models.views;

import lombok.Data;

@Data
public class CourseView {
    private int id;
    private String name;
    private String code;
    private String semester;
    private boolean active;

    private String tutorial;
}
