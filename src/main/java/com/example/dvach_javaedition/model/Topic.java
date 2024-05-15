package com.example.dvach_javaedition.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "section_article", referencedColumnName = "article")
    private Section section;

    @OneToMany(mappedBy = "topic")
    private List<Post> posts;
}
