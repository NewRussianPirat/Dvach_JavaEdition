package com.example.dvach_javaedition.service;

import com.example.dvach_javaedition.model.Section;
import com.example.dvach_javaedition.repository.SectionRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionService {
    @Autowired
    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public List<Section> findAll() {
        return sectionRepository.findAll(Sort.by(Sort.Order.asc("name")));
    }

    public Section findByArticle(String article) {
        Section section = sectionRepository.findByArticle(article);
        return sectionRepository.findByArticle(article);
    }

    public Section save(Section section) {
        return sectionRepository.save(section);
    }
}
