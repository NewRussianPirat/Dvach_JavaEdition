package com.example.dvach_javaedition.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileController {
    @GetMapping("/section/{sectionArticle}/{topicId}/{postId}/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable String sectionArticle,
                                              @PathVariable Long topicId,
                                              @PathVariable Long postId,
                                              @PathVariable String filename) {
        Path path = Paths.get("files/"
                + sectionArticle + "/"
                + topicId + "/"
                + postId + "/"
                + filename);
        Resource file;
        try {
            file = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
        return ResponseEntity.ok().body(file);
    }
}
