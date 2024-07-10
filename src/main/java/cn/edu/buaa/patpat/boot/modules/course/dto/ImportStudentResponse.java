package cn.edu.buaa.patpat.boot.modules.course.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ImportStudentResponse {
    private boolean status;
    private String message;

    private int created;
    private int updated;
    private int deleted;


    public static ImportStudentResponse of(String message) {
        return new ImportStudentResponse(false, message, 0, 0, 0);
    }

    public static ImportStudentResponse of(int created, int updated, int deleted) {
        return new ImportStudentResponse(true, "Import successfully", created, updated, deleted);
    }
}
