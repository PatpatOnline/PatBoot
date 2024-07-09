package cn.edu.buaa.patpat.boot.common.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HasTimestamp {
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
}
