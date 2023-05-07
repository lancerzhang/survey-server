package com.example.surveyserver.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileUploadControllerTest {

    @TempDir
    Path tempUploadDirectory;
    private FileUploadController fileUploadController;

    @BeforeEach
    public void setup() {
        fileUploadController = new FileUploadController();
        ReflectionTestUtils.setField(fileUploadController, "uploadDirectory", tempUploadDirectory.toString() + "/");
    }

    @Test
    public void testUploadFile() throws IOException {
        MockMultipartFile file = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image content".getBytes());
        ResponseEntity<String> response = fileUploadController.uploadFile(file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        String uploadedFilename = response.getBody();
        File uploadedFile = new File(tempUploadDirectory.toString(), uploadedFilename);
        assertTrue(uploadedFile.exists());
    }

    @Test
    public void testUploadEmptyFile() {
        MockMultipartFile file = new MockMultipartFile("image", "", "image/jpeg", new byte[0]);
        ResponseEntity<String> response = fileUploadController.uploadFile(file);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Please select a file to upload", response.getBody());
    }

    @Test
    public void testGetImage() throws IOException {
        String testContent = "test image content";
        String filename = "test.jpg";
        File testFile = new File(tempUploadDirectory.toString(), filename);
        Files.write(testFile.toPath(), testContent.getBytes());

        ResponseEntity<byte[]> response = fileUploadController.getImage(filename);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testContent, new String(response.getBody()));
    }

    @Test
    public void testGetNonExistentImage() throws IOException {
        String filename = "non_existent.jpg";
        ResponseEntity<byte[]> response = fileUploadController.getImage(filename);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
