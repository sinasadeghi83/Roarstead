package com.roarstead.Components.Resource.Models;

import com.roarstead.App;
import com.roarstead.Components.Database.Database;
import com.roarstead.Components.Exceptions.FileModelIsNotAnImageException;
import com.roarstead.Components.Resource.Converters.FileConverter;
import jakarta.persistence.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Entity
@Table(name="images")
public class Image {

    public static final String SIZE_UNIT = "Kilobytes";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "image_name")
    private String imageName;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "file_id", referencedColumnName = "id")
    private FileModel fileModel;

    private int height;

    private int width;

    public Image(){}

    public Image(String imageName, FileModel fileModel) throws FileModelIsNotAnImageException, IOException {
        if(!testFileForImage(fileModel.getFile()))
            throw new FileModelIsNotAnImageException(fileModel);
        this.imageName = imageName;
        this.fileModel = fileModel;
        this.height = processHeight();
        this.width = processWidth();
    }

    public Image(FileModel fileModel) throws FileModelIsNotAnImageException, IOException {
        if(!testFileForImage(fileModel.getFile()))
            throw new FileModelIsNotAnImageException(fileModel);
        this.fileModel = fileModel;
        this.imageName = fileModel.getFileName();
        this.height = processHeight();
        this.width = processWidth();
    }

    public static boolean testFileForImage(File file) throws IOException {
        return ImageIO.read(file) != null;
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public long imageSize() throws IOException {
        return this.fileModel.fileSize();
    }

    public int processHeight() throws IOException {
        BufferedImage bImg = ImageIO.read(fileModel.getFile());
        return bImg.getHeight();
    }

    public int processWidth() throws IOException {
        BufferedImage bImg = ImageIO.read(fileModel.getFile());
        return bImg.getWidth();
    }

}
