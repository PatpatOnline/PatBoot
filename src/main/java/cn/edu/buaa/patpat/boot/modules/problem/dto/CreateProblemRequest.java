package cn.edu.buaa.patpat.boot.modules.problem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class CreateProblemRequest {
    @NotNull
    @Size(min = 1, max = 255)
    private String title;

    @NotNull
    private String description;

    @NotNull
    private Boolean hidden;

    @NotNull
    private MultipartFile file;
}
