package cn.edu.buaa.patpat.boot.modules.problem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class UpdateProblemRequest {
    private String title;
    private String description;
    private Boolean hidden;
    private MultipartFile file;
}
