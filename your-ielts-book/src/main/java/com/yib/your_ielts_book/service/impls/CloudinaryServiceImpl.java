package com.yib.your_ielts_book.service.impls;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.yib.your_ielts_book.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;

    @Value("${cloudinary.upload-folder}")
    private String uploadFolder;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadWritingImage(MultipartFile image) throws IOException {
        Map<String, Object> uploadOptions = ObjectUtils.asMap(
                "folder", uploadFolder.endsWith("/") ?
                        uploadFolder + "WritingQuestionImage" :
                        uploadFolder + "/WritingQuestionImage",
                "resource_type", "image",
                "quality", "auto:good",
                "format", "jpg"
        );
        Map<?, ?> uploadResult = cloudinary.uploader().upload(image.getBytes(), uploadOptions);
        return (String) uploadResult.get("secure_url");
    }

    @Override
    public String uploadPdf(MultipartFile pdfFile) throws IOException {
        Map<String, Object> uploadOptions = ObjectUtils.asMap(
                "folder", uploadFolder.endsWith("/") ?
                        uploadFolder + "PdfFolder" :
                        uploadFolder + "/PdfFolder",
                "resource_type", "raw",  // MUST USE "raw" FOR PDFs
                "public_id", pdfFile.getOriginalFilename()  // Optional: Set a readable filename
        );

        // Log the upload attempt
        System.out.println("Uploading PDF: " + pdfFile.getOriginalFilename());

        Map<?, ?> uploadResult = cloudinary.uploader().upload(pdfFile.getBytes(), uploadOptions);

        // Log Cloudinary's response
        System.out.println("Cloudinary Response: " + uploadResult);

        return (String) uploadResult.get("secure_url");
    }

    @Override
    public String uploadAudio(MultipartFile audioFile) throws IOException {
        Map<String, Object> uploadOptions = ObjectUtils.asMap(
                "folder", uploadFolder.endsWith("/") ?
                        uploadFolder + "AudioFolder" :
                        uploadFolder + "/AudioFolder",
                "resource_type", "video"
        );
        Map<?, ?> uploadResult = cloudinary.uploader().upload(audioFile.getBytes(), uploadOptions);
        return (String) uploadResult.get("secure_url");
    }
}
