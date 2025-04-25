package com.yib.your_ielts_book.service.impls;

import com.yib.your_ielts_book.config.JWTService;
import com.yib.your_ielts_book.dto.ReadingAndListeningQuestionDTO;
import com.yib.your_ielts_book.dto.WritingQuestionDTO;
import com.yib.your_ielts_book.mapper.ReadingAndListeningQuestionMapper;
import com.yib.your_ielts_book.mapper.WritingQuestionMapper;
import com.yib.your_ielts_book.model.QuestionCategory;
import com.yib.your_ielts_book.model.ReadingAndListeningQuestion;
import com.yib.your_ielts_book.model.WritingQuestion;
import com.yib.your_ielts_book.repo.ReadingAndListeningQuestionRepo;
import com.yib.your_ielts_book.repo.WritingQuestionRepo;
import com.yib.your_ielts_book.service.CloudinaryService;
import com.yib.your_ielts_book.service.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final String FILE_STORAGE_PATH = "src/main/resources/static/uploads/";
    private final String PDF_DIR = "pdfs/";
    private final String AUDIO_DIR = "audios/";

    private final WritingQuestionRepo writingQuestionRepo;
    private final ReadingAndListeningQuestionRepo readingAndListeningQuestionRepo;
    private final CloudinaryService cloudinaryService;
    private final JWTService jwtService;
    private final ReadingAndListeningQuestionMapper readingAndListeningQuestionMapper;

    public QuestionServiceImpl(WritingQuestionRepo writingQuestionRepo, ReadingAndListeningQuestionRepo readingAndListeningQuestionRepo, CloudinaryService cloudinaryService, JWTService jwtService, ReadingAndListeningQuestionMapper readingAndListeningQuestionMapper) {
        this.writingQuestionRepo = writingQuestionRepo;
        this.readingAndListeningQuestionRepo = readingAndListeningQuestionRepo;
        this.cloudinaryService = cloudinaryService;
        this.jwtService = jwtService;
        this.readingAndListeningQuestionMapper = readingAndListeningQuestionMapper;
    }

    @Override
    public WritingQuestionDTO createWritingQues(WritingQuestionDTO dto, String jwt) {
        try {
            if (jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
            }
            int expertId = jwtService.extractUserId(jwt);
            String name = jwtService.extractFullName(jwt);
            if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
                try {
                    dto.setImageUrl(cloudinaryService.uploadWritingImage(dto.getImageFile()));
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image", e);
                }
            }
            WritingQuestion writingQuestion = WritingQuestionMapper.toEntity(dto);
            writingQuestion.setCategory(QuestionCategory.WRITING);
            writingQuestion.setExpertId(expertId);
            writingQuestion.setCreatedBy(name);
            writingQuestion.setCreatedAt(LocalDateTime.now());
            WritingQuestion savedQuestion = writingQuestionRepo.save(writingQuestion);
            return WritingQuestionMapper.toDTO(savedQuestion);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ReadingAndListeningQuestionDTO creatingReadingListeningQues(
            ReadingAndListeningQuestionDTO dto,
            MultipartFile pdfFile,
            MultipartFile audioFile,
            String jwt) {
        try {
            if (jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
            }
            int expertId = jwtService.extractUserId(jwt);
            String expertName = jwtService.extractFullName(jwt);

            createDirectoryIfNotExists(FILE_STORAGE_PATH + PDF_DIR);
            createDirectoryIfNotExists(FILE_STORAGE_PATH + AUDIO_DIR);

            dto.setExpertId(expertId);
            dto.setCreatedBy(expertName);
            dto.setCreatedAt(LocalDateTime.now());

            String pdfFilePath = null;
            if (pdfFile != null && !pdfFile.isEmpty()) {
                pdfFilePath = saveFile(pdfFile, PDF_DIR);
                dto.setPdfFilePath(pdfFilePath);
                dto.setPdfFileUrl("/api/files/" + PDF_DIR + getFileNameFromPath(pdfFilePath));
            }

            // Handle audio file upload if present
            String audioFilePath = null;
            if (audioFile != null && !audioFile.isEmpty()) {
                audioFilePath = saveFile(audioFile, AUDIO_DIR);
                dto.setListeningAudioUrl(audioFilePath);
                dto.setAudioFileUrl("/api/files/" + AUDIO_DIR + getFileNameFromPath(audioFilePath));
            }

            ReadingAndListeningQuestion savedQues = readingAndListeningQuestionMapper.toEntity(dto);
            return readingAndListeningQuestionMapper.toDTO(readingAndListeningQuestionRepo.save(savedQues));

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    // Function to save the file.
    private String saveFile(MultipartFile file, String directory) {
        try {
            // Generate a unique filename to prevent overwrite
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            String relativePath = directory + uniqueFilename;
            String absolutePath = FILE_STORAGE_PATH + relativePath;

            // Save the file to the specified location
            Path path = Paths.get(absolutePath);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Return the relative path to store in the database
            return relativePath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

//    Function to create directory
    private void createDirectoryIfNotExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private String getFileNameFromPath(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
