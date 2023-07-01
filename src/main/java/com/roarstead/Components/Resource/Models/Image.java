package com.roarstead.Components.Resource.Models;

import com.roarstead.Components.Exceptions.FileModelIsNotAnImageException;
import jakarta.persistence.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Entity
@Table(name="images")
public class Image extends Media{

    public static final String SIZE_UNIT = "Kilobytes";

    private int height;

    private int width;

    public Image(){}

    public Image(String mediaName, FileModel fileModel) throws FileModelIsNotAnImageException, IOException {
        super(mediaName, fileModel);
        this.height = processHeight();
        this.width = processWidth();
    }

    public Image(FileModel fileModel) throws FileModelIsNotAnImageException, IOException {
        super(fileModel.getFileName(), fileModel);
        this.height = processHeight();
        this.width = processWidth();
    }

    @Override
    public boolean testFileForMedia() throws IOException {
        return ImageIO.read(this.fileModel.getFile()) != null;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
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
