package cn.edu.buaa.patpat.boot.modules.group.dto;

import cn.edu.buaa.patpat.boot.aspect.IRequireValidation;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Data
public class UpdateGroupConfigRequest implements IRequireValidation {
    @Min(1)
    private Integer maxSize;

    @Min(0)
    @Max(200)
    private Integer minWeight;

    @Min(0)
    @Max(200)
    private Integer maxWeight;

    private Boolean enabled;

    @Override
    public void validate() {
        if (minWeight != null && maxWeight != null && minWeight > maxWeight) {
            throw new BadRequestException(M("group.weight.invalid"));
        }
    }
}
