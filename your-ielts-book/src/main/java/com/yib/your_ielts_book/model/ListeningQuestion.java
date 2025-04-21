package com.yib.your_ielts_book.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "listening_questions")
public class ListeningQuestion extends Question {
    private String audioFileUrl;
    private int audioDuration;
    private String transcript;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "listening_question_id")
    private List<ListeningQuestionItem> questions;
}
