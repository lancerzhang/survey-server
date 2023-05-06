package com.example.surveyserver.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
public class FileUploadController {

    private final String uploadDirectory = System.getProperty("user.dir") + "/uploads/";

    @PostMapping("/api/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("image") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a file to upload", HttpStatus.BAD_REQUEST);
        }

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDirectory, filename);

        try (InputStream inputStream = file.getInputStream()) {
            Files.createDirectories(path.getParent());
            Files.copy(inputStream, path);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to store file", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(filename, HttpStatus.OK);
    }

    @GetMapping("/api/image/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) throws IOException {
        File file = new File(uploadDirectory + filename);
        if (!file.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try (InputStream in = new FileInputStream(file)) {
            byte[] imageBytes = IOUtils.toByteArray(in);
            return new ResponseEntity<>(imageBytes, HttpStatus.OK);
        }
    }
}
