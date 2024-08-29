/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.account.models.mappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountFilter {
    private Integer id;

    private String buaaId;
    private String name;

    /**
     * 0: student, 1: teacher, 2: ta
     */
    private Integer role;
}
