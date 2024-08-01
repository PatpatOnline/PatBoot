package cn.edu.buaa.patpat.boot.modules.course.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAnnouncementRequest {
    @Size(min = 1, max = 255)
    @NotNull
    private String title;

    @Size(min = 1, max = 65535)
    @NotNull
    private String content;

    private Boolean topped;
}
