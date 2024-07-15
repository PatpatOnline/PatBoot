package cn.edu.buaa.patpat.boot.modules.judge.models;

import cn.edu.buaa.patpat.boot.config.Globals;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JudgeTimestamp {
    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime submitTime;

    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime startTime;

    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime endTime;
}
