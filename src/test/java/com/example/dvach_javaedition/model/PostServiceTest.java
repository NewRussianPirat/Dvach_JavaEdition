package com.example.dvach_javaedition.model;

import com.example.dvach_javaedition.dto.PostDTO;
import com.example.dvach_javaedition.service.PostService;
import com.example.dvach_javaedition.service.SectionService;
import com.example.dvach_javaedition.service.TopicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class PostServiceTest {
    @Autowired
    private TopicService topicService;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private PostService postService;

    @Test
    public void findAllByTopic() {
        Section section = sectionService.findByArticle("svo");
        List<Topic> topics = topicService.findAllBySection(section);
        Topic topic = topics.get(0);
        List<PostDTO> posts = postService.findAllPostDTOByTopic(topic);
        assertFalse(posts.isEmpty());
    }

    @Test
    public void findFirstAndThreeLastPosts() {
        Section section = sectionService.findByArticle("svo");
        List<Topic> topics = topicService.findAllBySection(section);
        Topic topic = topics.get(0);
        List<PostDTO> posts = postService.findFirstAndThreeLastPostsDTO(topic);
        assertFalse(posts.isEmpty());
        assertEquals(4, posts.size());
    }

    @Test
    public void countByTopic() {
        Section section = sectionService.findByArticle("svo");
        List<Topic> topics = topicService.findAllBySection(section);
        Topic topic = topics.get(0);
        Long count = postService.countByTopic(topic);
        assertEquals(count, 5);
    }
}
