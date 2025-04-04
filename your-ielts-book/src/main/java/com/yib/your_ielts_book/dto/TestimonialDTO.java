package com.yib.your_ielts_book.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestimonialDTO {
    private int testimonialId;

    @NotEmpty(message = "Your full name cannot be empty.")
    private String fullName;

    @Email(message = "Enter a valid email.")
    @NotEmpty(message = "Email is required.")
    private String email;

    @NotEmpty(message = "Your phone number cannot be empty.")
    @Pattern(regexp = "\\+?[0-9]{10}", message = "Enter a valid 10-digit phone number (with optional + at the beginning).")
    private String phoneNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date uploadedDate;

    @NotEmpty(message = "Comments cannot be empty.")
    @Size(max = 500, message = "Comments cannot exceed 500 characters.")
    private String comments;
}
