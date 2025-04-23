package com.example.demo.streamingModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/video")
@CrossOrigin // Allow cross-origin requests from the frontend
public class VideoController {
    @Autowired
    private VideoService videoService;

    @GetMapping("/stream")
    public ResponseEntity<String> getVideoStreamUrl() {
        // Return the URL to the HLS master playlist file
        String hlsUrl = videoService.getHlsStreamUrl();
        return new ResponseEntity<>(hlsUrl, HttpStatus.OK);
    }
}