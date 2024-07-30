package cn.edu.buaa.patpat.boot.modules.account.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordRequest {
    @Size(min = 6, max = 31)
    @NotNull
    private String oldPassword;

    @Size(min = 6, max = 31)
    @NotNull
    private String newPassword;
}
