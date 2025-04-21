package com.yib.your_ielts_book.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "reading_questions")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReadingQuestion extends Question {
    private String passage;
    private ReadingQuestionType type;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "reading_question_id")
    private List<ReadingQuestionOption> options;
    private String answer;
}
