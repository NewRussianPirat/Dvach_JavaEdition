package com.example.dvach_javaedition.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostWithFiles {
    private PostDTO post;
    List<FileDTO> files;
}
