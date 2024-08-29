/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.account.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Register is only enabled in development environment.
 */
@Data
public class UpdateAccountRequest {
    @Size(min = 1, max = 8)
    private String buaaId;

    @Size(min = 1, max = 31)
    private String name;

    @Min(0)
    @Max(2)
    private Integer gender;

    @Size(min = 1, max = 31)
    private String school;

    /**
     * 0: student, 1: ta, 3: teacher
     */
    @Min(0)
    @Max(2)
    private Integer role;
}
