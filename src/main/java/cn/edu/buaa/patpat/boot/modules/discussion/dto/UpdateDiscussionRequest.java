/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.discussion.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateDiscussionRequest {
    @Min(0)
    private Integer type;

    @Size(min = 1, max = 255)
    private String title;

    @Size(min = 1, max = 65535)
    private String content;
}
