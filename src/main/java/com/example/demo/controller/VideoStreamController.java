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
public class VideoStreamController {

    private final String videoBasePath = "/caminho/absoluto/para/seus/arquivos/videos"; // <== Ajuste aqui!

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> streamVideo(@PathVariable String fileName) throws Exception {
        // Monta o caminho do arquivo
        File file = new File(videoBasePath, fileName);

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        // Detecta o tipo do arquivo dinamicamente
        String contentType = detectMimeType(file.toPath(), fileName);

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
            // Tenta descobrir automaticamente
            String type = Files.probeContentType(path);
            return (type != null) ? type : "application/octet-stream";
        }
    }
}
