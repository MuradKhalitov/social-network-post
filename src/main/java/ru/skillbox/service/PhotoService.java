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
        // Логика сохранения файла
        String filePath = saveFile(file);

        // Сохранение пути к файлу и возврат DTO
        return new PhotoDto(filePath);
    }

    private String saveFile(MultipartFile file) {
        try {
            // Создание директории, если она не существует
            Path storagePath = Paths.get(STORAGE_DIR);
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath);  // Создание директории
            }

            // Генерация уникального имени файла и его сохранение
            Path path = storagePath.resolve(System.currentTimeMillis() + "_" + file.getOriginalFilename());
            Files.write(path, file.getBytes());
            return path.toString();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении файла", e);
        }
    }
}

