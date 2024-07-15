package cn.edu.buaa.patpat.boot.common.models;

import cn.edu.buaa.patpat.boot.config.Globals;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HasCreatedAndUpdated {
    @JsonFormat(pattern = Globals.DATE_FORMAT)
    protected LocalDateTime createdAt;

    @JsonFormat(pattern = Globals.DATE_FORMAT)
    protected LocalDateTime updatedAt;

    public HasCreatedAndUpdated() {
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

    public void update() {
        this.updatedAt = LocalDateTime.now();
    }
}