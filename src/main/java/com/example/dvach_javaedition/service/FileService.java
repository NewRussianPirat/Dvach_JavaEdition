package com.example.dvach_javaedition.service;

import com.example.dvach_javaedition.dto.FileDTO;
import com.example.dvach_javaedition.model.File;
import com.example.dvach_javaedition.model.Post;
import com.example.dvach_javaedition.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    @Autowired
    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<FileDTO> findAllFilesDTOByPost(Post post) {
        List<File> files = fileRepository.findAllByPost(post);
        return generateFileDTOList(files);
    }

    public List<File> findAllByPost(Post post) {
        return fileRepository.findAllByPost(post);
    }

    public List<FileDTO> findPostImages(Post post) {
        List<File> files = fileRepository.findAllByPostAndType(post, "image");
        return generateFileDTOList(files);
    }

    public List<FileDTO> findPostVideos(Post post) {
        List<File> files = fileRepository.findAllByPostAndType(post, "video");
        return generateFileDTOList(files);
    }

    public File save(File file) {
        return fileRepository.save(file);
    }

    public void deleteFile (File file) {
        fileRepository.deleteById(file.getId());
    }

    private List<FileDTO> generateFileDTOList(List<File> files) {
        List<FileDTO> returnFiles = new ArrayList<>();
        for (File file : files) {
            returnFiles.add(new FileDTO(file));
        }
        return returnFiles;
    }
}
