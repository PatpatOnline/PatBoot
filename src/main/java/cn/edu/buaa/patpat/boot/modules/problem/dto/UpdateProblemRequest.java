package cn.edu.buaa.patpat.boot.modules.problem.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class UpdateProblemRequest {
    @Size(min = 1, max = 255)
    private String title;

    private String description;

    private Boolean hidden;

    private MultipartFile file;
}
