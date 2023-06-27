package com.roarstead.Components.Resource.Converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.File;
import java.nio.file.Files;

@Converter
public class FileConverter implements AttributeConverter<File, String> {
    @Override
    public String convertToDatabaseColumn(File file) {
        return file != null ? file.getPath() : null;
    }

    @Override
    public File convertToEntityAttribute(String path) {
        return path != null ? new File(path) : null;
    }
}
