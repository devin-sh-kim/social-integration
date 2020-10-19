package net.ujacha.demo.social.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Audit {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private long created_by;
    private long updated_by;

}
