package com.example.Note.fileupload;
import com.example.Note.note.Note;
import com.example.Note.fileupload.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@RestController
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;

    }

    @GetMapping("/files/{noteId:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String noteId) throws Exception {

        final String uri = "http://localhost:8080/notes/"+noteId;

        RestTemplate restTemplate = new RestTemplate();
        Note uploadedNote = restTemplate.getForObject( uri, Note.class);


        Resource file = storageService.loadAsResource(uploadedNote.getLocation());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename*=UTF-8''" + URLEncoder.encode(file.getFilename(), "UTF-8")).header(HttpHeaders.CONTENT_TYPE,
                "application/octet-stream;charset=utf-8").body(file);
                //Todo charset不一定 utf-8 可以之後自己偵測
                //Todo 對於type 可以用工具自己偵測
    }


    @PostMapping("/upload")
    public Note  handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("noteName") String noteName,
                                   @RequestParam("format") String format,
                                   RedirectAttributes redirectAttributes) throws IOException {
        Path storePath = storageService.store(file);
        
        Note newNote = new Note();
        newNote.setNoteName(noteName);
        newNote.setClassName("uncategorized");
        newNote.setFormat(format);
        newNote.setLocation(storePath.toAbsolutePath().toString());

        final String uri = "http://localhost:8080/notes";

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Note> request = new HttpEntity<>(newNote);
        Note uploadedNote = restTemplate.postForObject( uri, request, Note.class);

        return uploadedNote;
    }
}