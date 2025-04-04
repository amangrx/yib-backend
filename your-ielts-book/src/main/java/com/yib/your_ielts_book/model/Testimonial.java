package com.yib.your_ielts_book.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "testimonials")
public class Testimonial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int testimonialId;

    private String fullName;
    private String email;
    private String phoneNumber;
    private Date uploadedDate;
    private String comments;
}
