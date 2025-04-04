package com.yib.your_ielts_book.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceDTO {
    private String additionalResourceName;
    private String additionalResourcePath;
    private String additionalResourceType;
    private String category;
    private String difficultyLevel;
    private String fileName;
    private String filePath;
    private String fileType;
    @NotEmpty(message = "Resource name cannot be empty.")
    private String resourceName;
    private int resourceId;
}
