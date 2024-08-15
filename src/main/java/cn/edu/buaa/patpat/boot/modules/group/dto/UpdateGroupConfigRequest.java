package cn.edu.buaa.patpat.boot.modules.group.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateGroupConfigRequest {
    @Min(1)
    private Integer maxSize;

    private Boolean enabled;
}
