package cn.edu.buaa.patpat.boot.modules.group.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateWeightRequest {
    @NotNull
    @Min(0)
    @Max(200)
    private int weight;
}
