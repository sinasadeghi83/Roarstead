package com.roarstead.Components.Resource.Models;

import com.google.common.io.Files;
import com.roarstead.App;
import com.roarstead.Components.Database.Database;
import com.roarstead.Components.Resource.Converters.FileConverter;
import jakarta.persistence.*;

import java.io.File;
import java.io.IOException;

@Entity
@Table(name = "files")
public class FileModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path")
    @Convert(converter = FileConverter.class)
    private File file;

    public FileModel(){
    }

    public FileModel(String fileName, File file) {
        this.fileName = fileName;
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public long fileSize() throws IOException {
        return Files.asByteSource(file).size() / 1024;
    }
}
