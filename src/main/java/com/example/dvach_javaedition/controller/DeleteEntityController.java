package com.example.dvach_javaedition.controller;

import com.example.dvach_javaedition.model.File;
import com.example.dvach_javaedition.model.Post;
import com.example.dvach_javaedition.model.Topic;
import com.example.dvach_javaedition.service.FileService;
import com.example.dvach_javaedition.service.PostService;
import com.example.dvach_javaedition.service.ServerFilesService;
import com.example.dvach_javaedition.service.TopicService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class DeleteEntityController {
    @Autowired
    TopicService topicService;

    @Autowired
    PostService postService;

    @Autowired
    FileService fileService;

    @Autowired
    ServerFilesService serverFilesService;

    @PostMapping({"/section/{article}/deleteTopic", "/section/{article}/topic/{topicId}/deleteTopic"})
    public String deleteTopic(@PathVariable("article") String article,
                              @PathVariable(value = "topicId", required = false) Long ignoredTopicId,
                              @RequestParam("topic") Long topic,
                              HttpServletRequest httpServletRequest) {
        String requestUrl = httpServletRequest.getRequestURI();

        Topic topicToDelete = topicService.findById(topic);
        List<Post> posts = postService.findAllByTopic(topicToDelete);
        for (Post post : posts) {
            List<File> files = fileService.findAllByPost(post);
            for (File file : files) {
                fileService.deleteFile(file);
            }
            postService.deletePost(post);
        }
        serverFilesService.delete(article, topic, Optional.empty());
        topicService.deleteTopic(topicToDelete);

        return "redirect:" + requestUrl.substring(0, requestUrl.lastIndexOf('/'));
    }

    @PostMapping({"/section/{article}/deletePost", "/section/{article}/topic/{topicId}/deletePost"})
    public String deletePost(@PathVariable("article") String article,
                              @PathVariable(value = "topicId", required = false) Long topicId,
                              @RequestParam("post") Long post,
                              HttpServletRequest httpServletRequest) {
        String requestUrl = httpServletRequest.getRequestURI();

        Post postToDelete = postService.findById(post);
        List<File> files = fileService.findAllByPost(postToDelete);
        for (File file : files) {
            fileService.deleteFile(file);
        }
        serverFilesService.delete(article, postToDelete.getTopic().getId(), Optional.of(post));
        postService.deletePost(postToDelete);

        return "redirect:" + requestUrl.substring(0, requestUrl.lastIndexOf('/'));
    }
}
