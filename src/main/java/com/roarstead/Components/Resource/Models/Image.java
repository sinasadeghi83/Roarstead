package com.roarstead.Components.Resource.Models;

import com.roarstead.Components.Resource.Converters.FileConverter;
import jakarta.persistence.*;

import java.io.File;

@Entity
@Table(name="images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "file_path")
    @Convert(converter = FileConverter.class)
    private File file;

    public Image(File file) {
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
