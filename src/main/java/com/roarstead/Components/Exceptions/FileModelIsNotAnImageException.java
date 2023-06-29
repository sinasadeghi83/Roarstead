package com.roarstead.Components.Exceptions;

import com.roarstead.Components.Resource.Models.FileModel;

public class FileModelIsNotAnImageException extends Exception {
    private FileModel fileModel;

    public FileModelIsNotAnImageException(){}

    public FileModelIsNotAnImageException(FileModel fileModel) {
        this.fileModel = fileModel;
    }

    public FileModel getFileModel() {
        return fileModel;
    }

    public void setFileModel(FileModel fileModel) {
        this.fileModel = fileModel;
    }
}
