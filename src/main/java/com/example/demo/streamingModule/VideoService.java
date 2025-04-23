package com.example.demo.streamingModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VideoService {
    @Value("${server.port:8080}")
    private String serverPort;

    private static final String HLS_VIDEO_PATH = "/movies/index.m3u8";

    public String getHlsStreamUrl() {
        return "http://localhost:" + serverPort + HLS_VIDEO_PATH;
    }
}