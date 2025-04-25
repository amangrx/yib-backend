package com.yib.your_ielts_book.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "reading_listening_questions")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@PrimaryKeyJoinColumn(name = "question_id")
public class ReadingAndListeningQuestion extends Question {
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String pdfFilePath;

    private String listeningAudioUrl;

    @ElementCollection
    @CollectionTable(
            name = "reading_listening_question_answers",
            joinColumns = @JoinColumn(name = "question_id")
    )
    @Column(name = "answers", nullable = false)
    private List<String> answers;
}
