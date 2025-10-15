package com.yib.your_ielts_book.dto;

import com.yib.your_ielts_book.model.ResourceStatus;
import com.yib.your_ielts_book.model.ResourceType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceDTO {
    private int resourceId;
    @NotEmpty(message = "Title cannot be empty.")
    private String title;
    @NotEmpty(message = "Category cannot be empty.")
    private String category;
    @NotEmpty(message = "Description cannot be empty.")
    private String description;
    private String pdfFilePath;
    private Date createdAt;
    private int expertId;
    private String author;
    private ResourceStatus status;
    private ResourceType type;
    private double price;
    // For frontend access (generated URLs)
    private String pdfFileUrl;
    // Transient fields for file upload (not persisted)
    private transient MultipartFile pdfFile;
}
