package com.example.Note.fileupload;
import com.example.Note.note.Note;
import com.example.Note.fileupload.storage.StorageService;
import com.example.Note.note.NoteRepository;
import com.example.Note.note.SameNoteNameFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Path;

@RestController
public class FileUploadController {

    private final StorageService storageService;
    @Autowired
    private final NoteRepository res;

    @Autowired
    public FileUploadController(StorageService storageService, NoteRepository res) {
        this.storageService = storageService;

        this.res = res;
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
                                   RedirectAttributes redirectAttributes) throws IOException,SameNoteNameFoundException {


        if(!res.findByName(noteName).isEmpty())
        {
            throw new SameNoteNameFoundException();
        }

        Path storePath = storageService.store(file,String.valueOf(System.currentTimeMillis())+file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".")) );
        Note newNote = new Note();
        newNote.setNoteName(noteName);
        newNote.setCategoryName("未分類");
        newNote.setFormat(format);
        newNote.setLocation(storePath.toAbsolutePath().toString());

        final String uri = "http://localhost:8080/notes";

        Note uploadedNote=null;

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Note> request = new HttpEntity<>(newNote);
        uploadedNote = restTemplate.postForObject( uri, request, Note.class);


        return uploadedNote;
    }
}
