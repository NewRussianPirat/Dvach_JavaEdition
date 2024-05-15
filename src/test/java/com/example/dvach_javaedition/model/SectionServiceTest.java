package com.example.dvach_javaedition.model;

import com.example.dvach_javaedition.service.SectionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class SectionServiceTest {
    @Autowired
    private SectionService sectionService;

    @Test
    public void getAll() {
        List<Section> sections = sectionService.findAll();
        assertFalse(sections.isEmpty());
    }

    @Test
    public void getByArticle() {
        String string = "svo";
        Section section = sectionService.findByArticle(string);
        assertEquals(string, section.getArticle());
    }
}
