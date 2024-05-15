package com.example.dvach_javaedition.repository;

import com.example.dvach_javaedition.model.File;
import com.example.dvach_javaedition.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findAllByPost(Post post);
    List<File> findAllByPostAndType(Post post, String filetype);
}
