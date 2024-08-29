/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.account.models.views;

import cn.edu.buaa.patpat.boot.common.models.HasCreated;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccountView extends HasCreated implements Serializable {
    private int id;
    private String buaaId;

    private String name;

    private boolean teacher;
    private boolean ta;
}
