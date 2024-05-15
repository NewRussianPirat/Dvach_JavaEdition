package com.example.dvach_javaedition.repository;

import com.example.dvach_javaedition.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    Section findByArticle(String article);
}
