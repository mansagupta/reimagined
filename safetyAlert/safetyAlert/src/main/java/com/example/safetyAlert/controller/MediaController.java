package com.example.safetyAlert.controller;

import com.example.safetyAlert.model.MediaFile;
import com.example.safetyAlert.service.MediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private static final Logger log = LoggerFactory.getLogger(MediaController.class);
    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadMedia(@RequestParam("file") MultipartFile file) {
        try {
            mediaService.storeMedia(file);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (IOException e) {
            log.error("e: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<MediaFile>> getAllMedia() {
        return ResponseEntity.ok(mediaService.getAllMedia());
    }
}
