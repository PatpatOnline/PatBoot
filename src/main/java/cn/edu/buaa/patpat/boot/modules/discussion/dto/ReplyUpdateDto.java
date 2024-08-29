/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.discussion.dto;

import cn.edu.buaa.patpat.boot.common.models.HasUpdated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReplyUpdateDto extends HasUpdated {
    private int id;
    private String content;
}
