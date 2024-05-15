package com.example.dvach_javaedition.controller;

import com.example.dvach_javaedition.dto.FileDTO;
import com.example.dvach_javaedition.dto.PostWithFiles;
import com.example.dvach_javaedition.dto.PostDTO;
import com.example.dvach_javaedition.model.File;
import com.example.dvach_javaedition.model.Post;
import com.example.dvach_javaedition.model.Section;
import com.example.dvach_javaedition.model.Topic;
import com.example.dvach_javaedition.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class SectionPageController {
    @Autowired
    private SectionService sectionService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ServerFilesService serverFilesService;

    @GetMapping("/section/{article}")
    public String getSection(@PathVariable("article") String article, Model model) {
        List<Section> sections = sectionService.findAll();
        model.addAttribute("sectionList", sections);

        Section section = sectionService.findByArticle(article);
        model.addAttribute("currentSection", section);

        List<Topic> topics = topicService.findAllBySection(section);
        model.addAttribute("topicList", topics);

        Map<Topic, List<PostDTO>> posts = new HashMap<>();
        for (Topic topic : topics) {
            if (postService.countByTopic(topic) < 5) {
                posts.put(topic, postService.findAllPostDTOByTopic(topic));
            } else {
                posts.put(topic, postService.findFirstAndThreeLastPostsDTO(topic));
            }
        }
        model.addAttribute("postMap", posts);

        Map<PostDTO, List<FileDTO>> images = new HashMap<>();
        Map<PostDTO, List<FileDTO>> videos = new HashMap<>();
        for (Topic topic : posts.keySet()) {
            for (Post post : topic.getPosts()) {
                videos.put(new PostDTO(post), fileService.findPostVideos(post));
                images.put(new PostDTO(post), fileService.findPostImages(post));
            }
        }
        model.addAttribute("videoMap", videos);
        model.addAttribute("imageMap", images);

        return "section";
    }

    @GetMapping("/section/{article}/topic/{topicId}/showAllPosts")
    public ResponseEntity<List<PostWithFiles>> showAllPosts(@PathVariable("article") String ignoredArticle,
                                                            @PathVariable("topicId") Long id) {
        Topic topic = topicService.findById(id);
        List<PostWithFiles> postWithFilesList = new ArrayList<>();
        for (Post post : postService.findAllByTopic(topic)) {
            postWithFilesList.add(new PostWithFiles(new PostDTO(post), fileService.findAllFilesDTOByPost(post)));
        }
        return ResponseEntity.ok(postWithFilesList);
    }

    @GetMapping("/section/{article}/topic/{topicId}/showNotAllPosts")
    public ResponseEntity<List<PostWithFiles>> showNotAllPosts(@PathVariable("article") String ignoredArticle,
                                                               @PathVariable("topicId") Long id) {
        Topic topic = topicService.findById(id);
        List<PostWithFiles> postWithFilesList = new ArrayList<>();
        for (Post post : postService.findFirstAndThreeLastPosts(topic)) {
            postWithFilesList.add(new PostWithFiles(new PostDTO(post), fileService.findAllFilesDTOByPost(post)));
        }
        return ResponseEntity.ok(postWithFilesList);
    }

    @PostMapping("/section/{article}/createTopic")
    public ResponseEntity<String> createTopic(@PathVariable("article") String article,
                                              @RequestParam("topicName") String topicName,
                                              @RequestParam("postContent") String postContent,
                                              @RequestParam(value = "file-input", required = false) MultipartFile[] files) {
        if (topicName == null
                || postContent == null
                || Objects.equals(topicName, "")
                || Objects.equals(postContent, "")) {
            return new ResponseEntity<>("Необходимые параметры отсутствуют", HttpStatus.BAD_REQUEST);
        }

        if (files != null) {
            for (var uploadedFile : files) {
                if (!(Objects.requireNonNull(uploadedFile.getContentType()).contains("image")
                        || uploadedFile.getContentType().contains("video"))) {
                    return new ResponseEntity<>("Можно отправлять только изображения и видео", HttpStatus.BAD_REQUEST);
                }
            }
        }

        LocalDateTime localDateTime = LocalDateTime.now();
        Section section = sectionService.findByArticle(article);

        Topic topic = Topic.builder()
                .section(section)
                .name(topicName)
                .build();

        Post post = Post.builder()
                .topic(topic)
                .content(postContent)
                .datetime(localDateTime)
                .build();

        topic = topicService.save(topic);
        post = postService.save(post);

        if (files != null) {
            List<File> files1 = serverFilesService.download(files, section, topic, post);
            for (File file : files1) {
                fileService.save(file);
            }
        }

        return ResponseEntity.ok("/section/" + article + "/topic/" + topic.getId());
    }
}
