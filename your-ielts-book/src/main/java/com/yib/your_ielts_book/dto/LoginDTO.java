package com.yib.your_ielts_book.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    @Email(message = "Enter a valid email.")
    @NotEmpty(message = "Email is required.")
    private String email;

    @NotEmpty(message = "Password is required.")
    private String password;
}
