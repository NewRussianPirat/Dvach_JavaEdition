package com.example.dvach_javaedition.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDateTime datetime;

    @ManyToOne
    private Topic topic;

    @OneToMany(mappedBy = "post")
    private List<File> files;

    public String getFormattedDatetime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy E HH:mm:ss", Locale.forLanguageTag("ru"));
        return datetime.format(formatter);
    }
}
