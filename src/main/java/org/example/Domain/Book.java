package org.example.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.VO.Student;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book_table")
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String author;
}
