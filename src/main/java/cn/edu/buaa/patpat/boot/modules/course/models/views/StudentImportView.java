package cn.edu.buaa.patpat.boot.modules.course.models.views;

import lombok.Data;

@Data
public class StudentImportView {
    /**
     * Student ID.
     */
    private int id;

    private int accountId;
    private int courseId;
    private int teacherId;

    private String buaaId;
}
