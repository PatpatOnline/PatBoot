package cn.edu.buaa.patpat.boot.modules.problem.dto;

import cn.edu.buaa.patpat.boot.common.utils.Strings;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class CreateProblemRequest {
    private String title;
    private String description;
    private boolean hidden;
    private MultipartFile file;

    public void validate() {
        if (Strings.isNullOrBlank(title)) {
            throw new BadRequestException("Problem title cannot be empty");
        }
        if (Strings.isNullOrBlank(description)) {
            throw new BadRequestException("Problem description cannot be empty");
        }
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Problem file cannot be empty");
        }
    }
}
