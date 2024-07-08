package cn.edu.buaa.patpat.boot.modules.account.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    /**
     * BUAA student ID.
     */
    @NotNull
    @Size(min = 1, max = 31)
    private String buaaId;

    @NotNull
    @Size(min = 1, max = 31)
    private String password;
}
