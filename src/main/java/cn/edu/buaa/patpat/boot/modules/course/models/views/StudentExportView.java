package cn.edu.buaa.patpat.boot.modules.course.models.views;

import lombok.Data;

@Data
public class StudentExportView {
    private String buaaId;
    private String name;
    private int gender;
    private String school;
    private String major;
    private String className;
    private boolean repeat;
}
