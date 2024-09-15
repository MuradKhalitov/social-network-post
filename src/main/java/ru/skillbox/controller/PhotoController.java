package ru.skillbox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.dto.photo.response.PhotoDto;
import ru.skillbox.service.PhotoService;

@RestController
@RequestMapping("/api/v1/post")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @PostMapping("/storagePostPhoto")
    public PhotoDto storePostPhoto(@RequestPart("file") MultipartFile file) {
        return photoService.uploadPhoto(file);
    }
}

