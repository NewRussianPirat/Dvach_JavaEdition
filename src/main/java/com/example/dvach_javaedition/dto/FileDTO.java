package com.example.dvach_javaedition.dto;

import com.example.dvach_javaedition.model.File;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FileDTO {
    private Long id;
    private String name;
    private String type;
    private Long size;

    public FileDTO(File file) {
        id = file.getId();
        name = file.getName();
        type = file.getType();
        size = file.getSize();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileDTO file = (FileDTO) o;
        return Objects.equals(id, file.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
