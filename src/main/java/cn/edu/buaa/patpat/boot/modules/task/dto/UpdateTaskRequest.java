/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.task.dto;

import cn.edu.buaa.patpat.boot.aspect.IRequireValidation;
import cn.edu.buaa.patpat.boot.common.Globals;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Data
public class UpdateTaskRequest implements IRequireValidation {
    @Size(min = 1, max = 255)
    private String title;

    @Size(min = 1)
    private String content;

    private Boolean visible;

    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime startTime;
    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime deadlineTime;
    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime endTime;

    /**
     * This should not be called in parameter validation, but in business logic.
     * Because these time fields are optional.
     * This is bad practice, I know. :(
     */
    @Override
    public void validate() {
        if (startTime != null && deadlineTime != null && endTime != null) {
            if (startTime.isAfter(deadlineTime) || startTime.isAfter(endTime) || deadlineTime.isAfter(endTime)) {
                throw new BadRequestException(M("task.time.invalid"));
            }
        }
    }
}
