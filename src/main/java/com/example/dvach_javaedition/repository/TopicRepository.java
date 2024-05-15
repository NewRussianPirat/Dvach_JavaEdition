package com.example.dvach_javaedition.repository;

import com.example.dvach_javaedition.model.Section;
import com.example.dvach_javaedition.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findAllBySectionOrderByIdDesc(Section section);
}
