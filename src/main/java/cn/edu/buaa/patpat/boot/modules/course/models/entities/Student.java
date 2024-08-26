package cn.edu.buaa.patpat.boot.modules.course.models.entities;

import cn.edu.buaa.patpat.boot.common.models.HasCreated;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class Student extends HasCreated implements Serializable {
    private int id;
    private int accountId;
    private int courseId;
    private int teacherId;

    private String major;
    private String className;
    private boolean repeat;
}
