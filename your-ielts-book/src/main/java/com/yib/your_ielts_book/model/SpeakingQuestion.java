package com.yib.your_ielts_book.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SpeakingQuestion extends Question {
    private SpeakingPart part;
    private String questionText;
    private String sampleAnswer;
}
