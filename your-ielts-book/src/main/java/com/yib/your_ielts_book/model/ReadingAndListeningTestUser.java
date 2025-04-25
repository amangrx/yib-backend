package com.yib.your_ielts_book.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reading_listening_test_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadingAndListeningTestUser extends Test {

}
