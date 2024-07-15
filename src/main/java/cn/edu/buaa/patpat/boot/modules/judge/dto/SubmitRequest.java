package cn.edu.buaa.patpat.boot.modules.judge.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class SubmitRequest {
    private int accountId;
    private String buaaId;
    private int courseId;
    private int problemId;
    private String language;
    
    private LocalDateTime submitTime;

    private MultipartFile file;

    public SubmitRequest(int accountId, String buaaId, int courseId, int problemId, String language, MultipartFile file) {
        this.accountId = accountId;
        this.buaaId = buaaId;
        this.courseId = courseId;
        this.problemId = problemId;
        this.language = language;
        this.file = file;
        this.submitTime = LocalDateTime.now();
    }
}
