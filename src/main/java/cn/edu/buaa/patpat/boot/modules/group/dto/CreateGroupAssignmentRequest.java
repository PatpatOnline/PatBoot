package cn.edu.buaa.patpat.boot.modules.group.dto;

import cn.edu.buaa.patpat.boot.aspect.IRequireValidation;
import cn.edu.buaa.patpat.boot.config.Globals;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Data
public class CreateGroupAssignmentRequest implements IRequireValidation {
    @NotNull
    @Size(max = 255)
    private String comment;

    @NotNull
    private Boolean visible;

    @NotNull
    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime startTime;

    @NotNull
    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime endTime;

    @Override
    public void validate() {
        if ((startTime == null) || (endTime == null) || startTime.isAfter(endTime)) {
            throw new BadRequestException(M("group.assignment.time.invalid"));
        }
    }
}
