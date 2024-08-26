package cn.edu.buaa.patpat.boot.modules.task.models.views;

import cn.edu.buaa.patpat.boot.common.Globals;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TaskListView implements Serializable {
    private int id;
    private String title;
    private boolean visible;

    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime startTime;
    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime deadlineTime;
    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime endTime;
}
