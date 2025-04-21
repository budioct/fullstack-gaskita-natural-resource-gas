package budhioct.dev.dto;

import budhioct.dev.entity.Folksy;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class FolksyDTO {

    @Getter
    @Setter
    @Builder
    public static class FolksyResponse {
        private Long id;
        private String name;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime updatedAt;
    }

    public static FolksyResponse toFolksyResponse(Folksy folksy) {
        return FolksyResponse.builder()
                .id(folksy.getId())
                .name(folksy.getName())
                .createdAt(folksy.getCreatedAt())
                .updatedAt(folksy.getUpdatedAt())
                .build();
    }

}
