package cn.edu.buaa.patpat.boot.modules.course.dto;

import cn.edu.buaa.patpat.boot.common.Globals;
import cn.edu.buaa.patpat.boot.modules.stream.dto.WebSocketPayload;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
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

    public WebSocketPayload<ImportStudentResponse> toWebSocketPayload() {
        return new WebSocketPayload<>(Globals.WS_IMPORT_STUDENT, this);
    }
}
