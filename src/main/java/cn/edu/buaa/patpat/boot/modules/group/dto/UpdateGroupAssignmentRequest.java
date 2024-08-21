package cn.edu.buaa.patpat.boot.modules.group.dto;

import cn.edu.buaa.patpat.boot.common.Globals;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateGroupAssignmentRequest {
    @Size(max = 255)
    private String comment;

    private Boolean visible;

    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime startTime;

    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime endTime;
}
