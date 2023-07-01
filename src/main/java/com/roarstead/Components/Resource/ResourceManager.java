package com.roarstead.Components.Resource;

import com.google.common.io.BaseEncoding;
import com.google.common.io.Files;
import com.google.common.net.MediaType;
import com.roarstead.App;
import com.roarstead.Components.Database.Database;
import com.roarstead.Components.Exceptions.BadRequestException;
import com.roarstead.Components.Exceptions.FileModelIsNotAnImageException;
import com.roarstead.Components.Exceptions.UnprocessableEntityException;
import com.roarstead.Components.Resource.Models.FileModel;
import com.roarstead.Components.Resource.Models.Image;
import com.roarstead.Components.Resource.Models.Media;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Random;

public class ResourceManager {
    public final String RES_PATH = "resources/";
    public static final Random random = new Random(10);

    List<FileItem> fileItems = null;

    public void retrieveDownloadedFiles() throws Exception {
        HttpExchange httpExchange = App.getCurrentApp().getHttpExchange();
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        fileItems = upload.parseRequest(new HttpHandlerRequestContext(httpExchange));
    }

    public long getFileItemSize(int fileItemIndex) {
        if(fileItemIndex >= fileItems.size()){
            throw new IndexOutOfBoundsException();
        }
        return fileItems.get(fileItemIndex).getSize() / 1024; // in Kilobytes
    }

    public FileModel saveFileItem(int index) throws Exception {
        if(index >= fileItems.size()){
            throw new IndexOutOfBoundsException();
        }
        return saveFileItem(fileItems.get(index));
    }

    public FileModel saveFileItem(FileItem fileItem) throws Exception {
        Database db = App.getCurrentApp().getDb();
        String fileName = fileItem.getName();
        String fileExt = Files.getFileExtension(fileName);
        // Save the file to disk
        File outputFile = new File(RES_PATH + Instant.now().getEpochSecond() + "_" + Math.abs(random.nextLong()) + "." + fileExt);
        fileItem.write(outputFile);
        // Save the file model to database
        db.ready();
        FileModel fileModel = new FileModel(fileName, outputFile);
        db.getSession().save(fileModel);
        db.done();
        return fileModel;
    }


    public List<FileItem> getFileItems() {
        return fileItems;
    }

    //TODO: Check image format
    public Image createImageFromFileModel(FileModel imageFileModel) throws FileModelIsNotAnImageException, IOException {
        return new Image(imageFileModel);
    }

    public Image createImageFromFileItem(FileItem fileItem) throws Exception {
        return createImageFromFileModel(saveFileItem(fileItem));
    }

    public Image createImageFromFileItem(int fileItemIndex) throws Exception {
        return createImageFromFileModel(saveFileItem(fileItemIndex));
    }

    public void deleteImage(Image profImage) {
        Database db = App.getCurrentApp().getDb();
        db.ready();
        db.getSession().remove(profImage);
        db.done();
        deleteFileModel(profImage.getFileModel());
    }

    public void deleteFileModel(FileModel fileModel) {
        Database db = App.getCurrentApp().getDb();
        db.ready();
        db.getSession().remove(fileModel);
        db.done();
        fileModel.getFile().delete();
    }

    public Image retrieveUploadedImage() throws Exception {
        Database db = App.getCurrentApp().getDb();
        //Retrieve uploaded image
        Image image;
        try {
            db.ready();
            image = createImageFromFileItem(0);
            db.getSession().persist(image);
            db.done();
        } catch (FileModelIsNotAnImageException e){
            deleteFileModel(e.getFileModel());
            throw new UnprocessableEntityException("File is not an image!");
        } catch (Exception e) {
            throw new BadRequestException();
        }
        return image;
    }

    public String convertMediaToBase64(Media media) throws Exception {
        if(media == null)
            return "";
        File file = media.getFileModel().getFile();
        if (file.exists()) {
            byte[] fileContent = Files.toByteArray(file);
            return BaseEncoding.base64().encode(fileContent);
        } else {
            return "";
        }
    }

    public String getMediaMIMEType(Media media) throws Exception{
        File file = media.getFileModel().getFile();

        Path filePath = file.toPath();
        String mimeType = java.nio.file.Files.probeContentType(filePath);

        MediaType mediaType = MediaType.parse(mimeType);
        return mediaType.toString();
    }

    class HttpHandlerRequestContext implements RequestContext{

        HttpExchange httpExchange;

        public HttpHandlerRequestContext(HttpExchange httpExchange){
            this.httpExchange = httpExchange;
        }

        @Override
        public String getCharacterEncoding() {
            return "UTF-8";
        }

        @Override
        public int getContentLength() {
            return 0; //tested to work with 0 as return
        }

        @Override
        public String getContentType() {
            return httpExchange.getRequestHeaders().getFirst("Content-type");
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return httpExchange.getRequestBody();
        }
    }
}
