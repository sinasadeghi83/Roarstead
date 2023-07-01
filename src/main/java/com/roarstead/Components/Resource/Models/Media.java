package com.roarstead.Components.Resource.Models;

import com.roarstead.Components.Exceptions.FileModelIsNotAnImageException;
import jakarta.persistence.*;

import java.io.File;
import java.io.IOException;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected int id;

    @Column(name = "media_name")
    protected String mediaName;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "file_id", referencedColumnName = "id")
    protected FileModel fileModel;

    public Media(){}

    public Media(String mediaName, FileModel fileModel) throws FileModelIsNotAnImageException, IOException {
        this.mediaName = mediaName;
        this.fileModel = fileModel;
        if (!testFileForMedia())
            throw new FileModelIsNotAnImageException(fileModel);
    }

    public abstract boolean testFileForMedia() throws IOException;

    public FileModel getFileModel() {
        return fileModel;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public long mediaSize() throws IOException {
        return this.fileModel.fileSize();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExtension() {
        return fileModel.getExtension();
    }
}
