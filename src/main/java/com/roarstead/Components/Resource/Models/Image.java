package com.roarstead.Components.Resource.Models;

import com.roarstead.App;
import com.roarstead.Components.Database.Database;
import com.roarstead.Components.Resource.Converters.FileConverter;
import jakarta.persistence.*;

import java.io.File;

@Entity
@Table(name="images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "image_name")
    private String imageName;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "file_id", referencedColumnName = "id")
    private FileModel fileModel;

    public Image(){}

    public Image(String imageName, FileModel fileModel) {
        this.imageName = imageName;
        this.fileModel = fileModel;
    }

    public Image(FileModel fileModel){
        this.fileModel = fileModel;
        this.imageName = fileModel.getFileName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FileModel getFileModel() {
        return fileModel;
    }

    public void setFileModel(FileModel fileModel) {
        this.fileModel = fileModel;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
