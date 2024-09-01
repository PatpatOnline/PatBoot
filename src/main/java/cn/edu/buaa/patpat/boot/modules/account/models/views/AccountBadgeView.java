/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.account.models.views;

import lombok.Data;

// This is used to display user information in the badge.
@Data
public class AccountBadgeView {
    private int id;
    private String buaaId;
    private String name;
    private String avatar;
}
