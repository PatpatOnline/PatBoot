package cn.edu.buaa.patpat.boot.modules.group.dto;

import cn.edu.buaa.patpat.boot.config.Globals;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupAssignmentDto {
    private String comment;

    private boolean visible;

    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime startTime;

    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime endTime;
}
