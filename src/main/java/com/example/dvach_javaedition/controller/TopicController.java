package com.example.dvach_javaedition.controller;

import com.example.dvach_javaedition.dto.FileDTO;
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
public class TopicController {
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

    @GetMapping("/section/{article}/topic/{topicId}")
    public String getSection(@PathVariable("article") String article,
                             @PathVariable("topicId") Long topicId,
                             Model model) {
        List<Section> sections = sectionService.findAll();
        model.addAttribute("sectionList", sections);

        Section section = sectionService.findByArticle(article);
        model.addAttribute("currentSection", section);

        Topic topic = topicService.findById(topicId);
        model.addAttribute("currentTopic", topic);

        List<PostDTO> posts = postService.findAllPostDTOByTopic(topic);
        model.addAttribute("postList", posts);

        Map<PostDTO, List<FileDTO>> images = new HashMap<>();
        Map<PostDTO, List<FileDTO>> videos = new HashMap<>();
        for (Post post : topic.getPosts()) {
            videos.put(new PostDTO(post), fileService.findPostVideos(post));
            images.put(new PostDTO(post), fileService.findPostImages(post));
        }
        model.addAttribute("videoMap", videos);
        model.addAttribute("imageMap", images);

        return "topic";
    }

    @PostMapping("/section/{article}/topic/{topicId}/answer")
    public ResponseEntity<String> answerTopic(@PathVariable("article") String article,
                                      @PathVariable("topicId") Long topicId,
                                      @RequestParam("postContent") String postContent,
                                      @RequestParam(value = "file-input", required = false) MultipartFile[] files) {
        if (postContent == null || Objects.equals(postContent, "")) {
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

        Topic topic = topicService.findById(topicId);

        Post post = Post.builder()
                .topic(topic)
                .content(postContent)
                .datetime(localDateTime)
                .build();

        post = postService.save(post);

        if (files != null) {
            List<File> files1 = serverFilesService.download(files, section, topic, post);
            for (File file : files1) {
                fileService.save(file);
            }
        }

        return ResponseEntity.ok("/section/" + article + "/topic/" + topicId);
    }
}
