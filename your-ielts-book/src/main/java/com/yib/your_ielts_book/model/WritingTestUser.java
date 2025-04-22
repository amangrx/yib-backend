package com.yib.your_ielts_book.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "writing_test_users")
@Data
@PrimaryKeyJoinColumn(name = "tests_id")
@AllArgsConstructor
@NoArgsConstructor
public class WritingTestUser extends Test{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private WritingQuestion writingQuestion;

    @Column(columnDefinition = "TEXT")
    private String answer;

    private double score;

    @Column(columnDefinition = "TEXT")
    private String feedback;
}
