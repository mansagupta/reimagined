package com.example.safetyAlert.service;

import com.example.safetyAlert.model.MediaFile;
import com.example.safetyAlert.repository.MediaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class MediaService {

    private final MediaRepository mediaRepository;
    private final String uploadDir = System.getProperty("user.dir") + "/uploads/";


    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public void storeMedia(MultipartFile file) throws IOException {
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        Path filePath = Paths.get(uploadDir).resolve(file.getOriginalFilename());
        file.transferTo(filePath.toFile());

        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileName(file.getOriginalFilename());
        mediaFile.setFileType(file.getContentType());
        mediaFile.setFilePath(filePath.toString());

        mediaRepository.save(mediaFile);
    }


    public List<MediaFile> getAllMedia() {
        return mediaRepository.findAll();
    }
}

