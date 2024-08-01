package cn.edu.buaa.patpat.boot.modules.course.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateAnnouncementRequest {
    @Size(min = 1, max = 255)
    private String title;

    @Size(min = 1, max = 65535)
    private String content;

    private Boolean topped;
}
