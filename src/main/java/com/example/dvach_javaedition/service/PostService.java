package com.example.dvach_javaedition.service;

import com.example.dvach_javaedition.dto.PostDTO;
import com.example.dvach_javaedition.model.Post;
import com.example.dvach_javaedition.model.Topic;
import com.example.dvach_javaedition.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post findById(Long id) {
        var post = postRepository.findById(id);

        return post.orElse(null);
    }

    public List<PostDTO> findFirstAndThreeLastPostsDTO(Topic topic) {
        List<Post> posts = postRepository.findFirstAndThreeLastPosts(topic.getId());
        return generatePostDTOList(posts);
    }

    public List<PostDTO> findAllPostDTOByTopic(Topic topic) {
        List<Post> posts = postRepository.findAllByTopicOrderByDatetimeAsc(topic);
        return generatePostDTOList(posts);
    }

    public List<Post> findAllByTopic(Topic topic) {
        return postRepository.findAllByTopicOrderByDatetimeAsc(topic);
    }

    public List<Post> findFirstAndThreeLastPosts(Topic topic) {
        return postRepository.findFirstAndThreeLastPosts(topic.getId());
    }

    public Long countByTopic(Topic topic) {
        return postRepository.countByTopic(topic);
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public void deletePost (Post post) {
        postRepository.deleteById(post.getId());
    }

    private List<PostDTO> generatePostDTOList(List<Post> posts) {
        List<PostDTO> returnPosts = new ArrayList<>();
        for (Post post : posts) {
            returnPosts.add(new PostDTO(post));
        }
        return returnPosts;
    }
}
