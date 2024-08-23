package cn.edu.buaa.patpat.boot.modules.task.dto;

import cn.edu.buaa.patpat.boot.aspect.IRequireValidation;
import cn.edu.buaa.patpat.boot.common.Globals;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Data
public class CreateTaskRequest implements IRequireValidation {
    @NotNull
    @Size(min = 1, max = 255)
    private String title;

    @NotNull
    @Size(min = 1, max = 65535)
    private String content;

    @NotNull
    private Boolean visible;

    @NotNull
    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime startTime;

    @NotNull
    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime deadlineTime;

    @NotNull
    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime endTime;

    @Override
    public void validate() {
        if (startTime.isAfter(deadlineTime) || startTime.isAfter(endTime) || deadlineTime.isAfter(endTime)) {
            throw new BadRequestException(M("task.time.invalid"));
        }
    }
}
