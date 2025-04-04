package com.yib.your_ielts_book.dto;

import com.yib.your_ielts_book.model.UserRole;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private int customerId;

    @NotEmpty(message = "Address cannot be empty.")
    private String address;

    @Email(message = "Enter a valid email.")
    @NotEmpty(message = "Email is required.")
    private String email;

    @NotEmpty(message = "Name cannot be empty.")
    private String name;

    @NotEmpty(message = "Password is required.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,12}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
    )
    @Size(max = 12, min = 6, message = "Password must be of 6-12 characters")
    private String password;

    @NotEmpty(message = "Your phone number cannot be empty.")
    @Pattern(regexp = "\\+?[0-9]{10,15}", message = "Enter a valid phone number.")
    private String phoneNumber;

    private String pictureName;
    private String pictureType;
    private byte[] profilePicture;

    private UserRole role;
}
