package cn.edu.buaa.patpat.boot.modules.course.models.views;

import cn.edu.buaa.patpat.boot.common.models.HasCreated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentListView extends HasCreated {
    private int id;
    private int accountId;
    private String buaaId;

    /**
     * In {@link StudentDetailView}, the field for the name of the student is
     * named {@code studentName} to distinguish from {@code teacherName}.
     * To maintain consistency, this field is named {@code studentName} instead of
     * {@code name} even if the teacher's name is replaced by the id.
     */
    private String studentName;
    private int teacherId;

    private String school;
    private String major;
    private String className;
    private boolean repeat;
}
