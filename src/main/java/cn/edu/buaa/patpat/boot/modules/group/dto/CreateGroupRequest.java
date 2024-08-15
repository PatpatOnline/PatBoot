package cn.edu.buaa.patpat.boot.modules.group.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateGroupRequest {
    @Size(min = 1, max = 63)
    @NotNull
    private String name;

    @Size(max = 255)
    @NotNull
    private String description;
}
