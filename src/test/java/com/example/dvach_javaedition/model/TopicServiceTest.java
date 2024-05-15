package com.example.dvach_javaedition.model;

import com.example.dvach_javaedition.service.SectionService;
import com.example.dvach_javaedition.service.TopicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class TopicServiceTest {
    @Autowired
    private TopicService topicService;
    @Autowired
    private SectionService sectionService;

    @Test
    public void findAllBySection() {
        Section section = sectionService.findByArticle("svo");
        List<Topic> topics = topicService.findAllBySection(section);
        assertFalse(topics.isEmpty());
    }
}
