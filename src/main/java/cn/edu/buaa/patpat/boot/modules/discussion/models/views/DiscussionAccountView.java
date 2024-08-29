/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.discussion.models.views;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscussionAccountView implements Serializable {
    @JsonIgnore
    private int id;

    private String buaaId;
    private String name;
    private String avatar;
    private boolean isTa;
    private boolean isTeacher;
}
