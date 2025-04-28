package com.example.demo.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/stream")
@CrossOrigin(origins = "http://localhost:5173")
public class VideoStreamController {
    private final String basePath = "/Users/reoshiro/Documents/POC/streaming-poc/bucket";

    @GetMapping("/videos/{*path}")
    public ResponseEntity<Resource> streamVideo(@PathVariable("path") String path) throws Exception {
        File file = new File(basePath + "/videos/" + path);

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        String contentType = detectMimeType(file.toPath(), file.getName());
        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @GetMapping("/thumbnails/{filename:.+}")
    public ResponseEntity<Resource> streamThumbnail(@PathVariable String filename) throws Exception {
        File file = new File(basePath + "/thumbnails/" + filename);

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        String contentType = Files.probeContentType(file.toPath());
        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    private String detectMimeType(Path path, String fileName) throws Exception {
        if (fileName.endsWith(".m3u8")) {
            return "application/vnd.apple.mpegurl";
        } else if (fileName.endsWith(".ts")) {
            return "video/MP2T";
        } else {
            String type = Files.probeContentType(path);
            return (type != null) ? type : "application/octet-stream";
        }
    }
}
