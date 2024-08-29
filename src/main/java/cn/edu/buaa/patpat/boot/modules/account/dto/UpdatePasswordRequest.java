/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordRequest {
    @NotNull
    @Size(min = 1, max = 31)
    @JsonProperty("old")
    private String oldPassword;

    @NotNull
    @Size(min = 6, max = 31)
    @JsonProperty("new")
    private String newPassword;
}
