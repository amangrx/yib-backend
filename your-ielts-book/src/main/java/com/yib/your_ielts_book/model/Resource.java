package com.yib.your_ielts_book.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "resources")
public class Resource {
    private String additionalResourceName;
    private String additionalResourcePath;
    private String additionalResourceType;

    @Column(nullable = false)
    private String category;

    private String fileName;
    @Column(nullable = false)
    private String filePath;
    private String fileType;

    @Column( nullable = false)
    private String resourceName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int resourceId;
}
