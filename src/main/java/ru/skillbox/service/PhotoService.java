package ru.skillbox.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.dto.photo.response.PhotoDto;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

@Service
public class PhotoService {

    private static final String STORAGE_DIR = "photos/";

    public PhotoDto uploadPhoto(MultipartFile file) {
        String filePath = saveFile(file);
        return new PhotoDto(filePath);
    }

    private String saveFile(MultipartFile file) {
        try {
            Path storagePath = Paths.get(STORAGE_DIR);
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath);
            }
            Path path = storagePath.resolve(System.currentTimeMillis() + "_" + file.getOriginalFilename());
            Files.write(path, file.getBytes());
            return path.toString();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении файла", e);
        }
    }
}

