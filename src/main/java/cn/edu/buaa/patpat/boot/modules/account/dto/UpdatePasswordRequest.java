package cn.edu.buaa.patpat.boot.modules.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordRequest {
    @Size(min = 1, max = 31)
    @NotNull
    @JsonProperty("old")
    private String oldPassword;

    @Size(min = 6, max = 31)
    @NotNull
    @JsonProperty("new")
    private String newPassword;
}
