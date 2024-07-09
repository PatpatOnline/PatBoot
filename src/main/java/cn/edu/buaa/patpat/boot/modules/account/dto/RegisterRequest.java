package cn.edu.buaa.patpat.boot.modules.account.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Register is only enabled in development environment.
 */
@Data
public class RegisterRequest {
    @Size(min = 1, max = 8)
    private String buaaId;

    @Size(min = 1, max = 31)
    private String name;

    @Size(min = 1, max = 31)
    private String password;

    @Min(0)
    @Max(2)
    private int gender;

    @Size(min = 1, max = 31)
    private String school;
    private boolean teacher;
    private boolean ta;
}
