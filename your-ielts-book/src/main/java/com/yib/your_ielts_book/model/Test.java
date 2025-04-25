package com.yib.your_ielts_book.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tests")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int customerId;

    private LocalDateTime submittedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestStatus status;

    private int duration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionCategory questionCategory;

    @Column(name = "question_id", nullable = false)
    private Integer questionId;
}
