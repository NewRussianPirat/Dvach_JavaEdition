package com.example.dvach_javaedition.service;

import com.example.dvach_javaedition.model.File;
import com.example.dvach_javaedition.model.Post;
import com.example.dvach_javaedition.model.Section;
import com.example.dvach_javaedition.model.Topic;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ServerFilesService {
    public List<File> download(MultipartFile[] files, Section section, Topic topic, Post post) {
        List<File> files1 = new ArrayList<>();
        for (var uploadedFile : files) {
            String type = "";
            if (Objects.requireNonNull(uploadedFile.getContentType()).contains("image")) {
                type = "image";
            } else if (uploadedFile.getContentType().contains("video")) {
                type = "video";
            }

            long CONVERT_TO_KB = 1024L;
            File file = File.builder()
                    .name(uploadedFile.getOriginalFilename())
                    .type(type)
                    .size(uploadedFile.getSize() / CONVERT_TO_KB)
                    .post(post)
                    .build();

            files1.add(file);

            String rootPath = System.getProperty("user.dir");
            String filePath = rootPath + "/files/"
                    + section.getArticle() + "/"
                    + topic.getId().toString() + "/"
                    + post.getId().toString() + "/"
                    + uploadedFile.getOriginalFilename();
            java.nio.file.Path path = Paths.get(filePath);
            try {
                Files.createDirectories(path.getParent());
                Files.write(path, uploadedFile.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return files1;
    }

    public void delete(String sectionArticle, Long topicId, Optional<Long> postId) {
        String rootPath = System.getProperty("user.dir");
        String filePath = rootPath + "/files/"
                + sectionArticle + "/"
                + topicId.toString() + "/"
                + (postId.map(aLong -> aLong.toString() + "/").orElse(""));

        Path path = Paths.get(filePath);
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
