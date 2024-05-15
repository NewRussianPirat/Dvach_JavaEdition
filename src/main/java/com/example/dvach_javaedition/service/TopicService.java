package com.example.dvach_javaedition.service;

import com.example.dvach_javaedition.model.Section;
import com.example.dvach_javaedition.model.Topic;
import com.example.dvach_javaedition.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {
    @Autowired
    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<Topic> findAllBySection(Section section) {
        return topicRepository.findAllBySectionOrderByIdDesc(section);
    }

    public Topic findById(Long id) {
        var topic = topicRepository.findById(id);
        return topic.orElse(null);
    }

    public Topic save(Topic topic) {
        return topicRepository.save(topic);
    }

    public void deleteTopic (Topic topic) {
        topicRepository.deleteById(topic.getId());
    }
}
