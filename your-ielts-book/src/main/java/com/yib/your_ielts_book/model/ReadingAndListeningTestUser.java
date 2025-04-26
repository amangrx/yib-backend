package com.yib.your_ielts_book.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "reading_listening_test_users")
@Data
@PrimaryKeyJoinColumn(name = "test_id")
@AllArgsConstructor
@NoArgsConstructor
public class ReadingAndListeningTestUser extends Test {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private ReadingAndListeningQuestion question;

    @ElementCollection
    @CollectionTable(
            name = "user_reading_listening_answers",
            joinColumns = @JoinColumn(name = "test_id")
    )
    @Column(name = "answer")
    private List<String> answers;

    private double score;

    @Column(columnDefinition = "TEXT")
    private String feedback;
}