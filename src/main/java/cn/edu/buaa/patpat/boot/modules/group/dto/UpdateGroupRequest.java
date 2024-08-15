package cn.edu.buaa.patpat.boot.modules.group.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateGroupRequest {
    @Size(min = 1, max = 63)
    private String name;

    @Size(max = 255)
    private String description;

    private Boolean locked;
}
