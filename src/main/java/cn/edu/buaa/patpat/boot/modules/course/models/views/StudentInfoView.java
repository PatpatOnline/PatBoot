package cn.edu.buaa.patpat.boot.modules.course.models.views;

import lombok.Data;

import java.io.Serializable;

@Data
public class StudentInfoView implements Serializable {
    private int studentId;
    private int accountId;
    private String buaaId;
    private String name;
}
