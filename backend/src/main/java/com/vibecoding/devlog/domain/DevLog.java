package com.vibecoding.devlog.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DevLog {
    private Long id;
    private Long projectId;
    private String title;
    private String content;
    private String tags;
    private LocalDateTime logDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
