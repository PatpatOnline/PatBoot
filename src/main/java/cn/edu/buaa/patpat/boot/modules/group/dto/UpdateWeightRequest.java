package cn.edu.buaa.patpat.boot.modules.group.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateWeightRequest {
    @Min(0)
    @Max(200)
    @NotNull
    private int weight;
}
