package com.yib.your_ielts_book.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    String uploadWritingImage(MultipartFile image) throws IOException;

    String uploadPdf(MultipartFile pdfFile) throws IOException;
    String uploadAudio(MultipartFile audioFile) throws IOException;
}
