package cn.edu.buaa.patpat.boot.common.models;

import cn.edu.buaa.patpat.boot.config.Globals;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HasUpdated {
    @JsonFormat(pattern = Globals.DATE_FORMAT)
    protected LocalDateTime updatedAt;

    public void update() {
        this.updatedAt = LocalDateTime.now();
    }
}
