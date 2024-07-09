package cn.edu.buaa.patpat.boot.common.models;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * A model that has a created time.
 */
@Data
public class HasCreated {
    @DateTimeFormat
    protected LocalDateTime createdAt;

    public HasCreated() {
        this.createdAt = LocalDateTime.now();
    }
}
