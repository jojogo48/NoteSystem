package com.example.Note.controller;
import com.example.Note.entity.Note;
import com.example.Note.exception.NoAccessToFileException;
import com.example.Note.repository.CategoryRepository;
import com.example.Note.service.AuthenticationService;
import com.example.Note.service.StorageService;
import com.example.Note.repository.NoteRepository;
import com.example.Note.exception.SameNoteNameFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

@RestController
public class FileUploadController {
    @Autowired
    private AuthenticationService auService;

    private final StorageService storageService;
    @Autowired
    private final NoteRepository res;
    @Autowired
    private final CategoryRepository resCat;

    public FileUploadController(AuthenticationService auService, StorageService storageService, NoteRepository res, CategoryRepository resCat) {
        this.auService = auService;
        this.storageService = storageService;
        this.res = res;
        this.resCat = resCat;
    }

    @Autowired
    public FileUploadController(StorageService storageService, NoteRepository res, CategoryRepository resCat) {
        this.storageService = storageService;

        this.res = res;
        this.resCat = resCat;
    }


    @GetMapping("/files/download/{token:.+}/{noteId:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String noteId,@PathVariable String token) throws Exception {

        if(!auService.isNoteBelongToUser(Long.parseLong(noteId),auService.getUid(token))){
            throw new NoAccessToFileException();
        }

        Optional<Note> uploadedNoteOpt = res.findById(Long.parseLong(noteId));
        if (!uploadedNoteOpt.isPresent()) {
            throw new Exception("no file found!!");
        }
        Note uploadedNote = uploadedNoteOpt.get();

        Resource file = storageService.loadAsResource(uploadedNote.getLocation());


        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename*=UTF-8''" + URLEncoder.encode(file.getFilename(), "UTF-8")).header(HttpHeaders.CONTENT_TYPE,
                "application/octet-stream;charset=utf-8").body(file);
        //Todo charset不一定 utf-8 可以之後自己偵測
        //Todo 對於type 可以用工具自己偵測
    }

    @GetMapping("/files/{token:.+}/{noteId:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String noteId,@PathVariable String token) throws Exception {



        if(!auService.isNoteBelongToUser(Long.parseLong(noteId),auService.getUid(token))){
            throw new NoAccessToFileException();
        }
        Optional<Note> uploadedNoteOpt = res.findById(Long.parseLong(noteId));
        if (!uploadedNoteOpt.isPresent()) {
            throw new Exception("no file found!!");
        }
        Note uploadedNote = uploadedNoteOpt.get();


        Resource file = storageService.loadAsResource(uploadedNote.getLocation());

        if(uploadedNote.getFormat().equals("text"))
        {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "inline;filename*=UTF-8''" + URLEncoder.encode(file.getFilename(), "UTF-8")).header(HttpHeaders.CONTENT_TYPE,
                    "text/plain;charset=utf-8").body(file);

        }
        if(uploadedNote.getFormat().equals("pdf"))
        {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "inline;filename*=UTF-8''" + URLEncoder.encode(file.getFilename(), "UTF-8")).header(HttpHeaders.CONTENT_TYPE,
                    "application/pdf;charset=utf-8").body(file);
        }
        if(uploadedNote.getFormat().equals("picture"))
        {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "inline;filename*=UTF-8''" + URLEncoder.encode(file.getFilename(), "UTF-8")).header(HttpHeaders.CONTENT_TYPE,
                    "image/png;charset=utf-8").body(file);
        }
        if(uploadedNote.getFormat().equals("html"))
        {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "inline;filename*=UTF-8''" + URLEncoder.encode(file.getFilename(), "UTF-8")).header(HttpHeaders.CONTENT_TYPE,
                    "text/html;charset=utf-8").body(file);
        }
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
                                   @RequestParam("noteDescription") String noteDescription,
                                   @RequestParam("token") String token,
                                   RedirectAttributes redirectAttributes) throws IOException,SameNoteNameFoundException {




        if(!res.findByNameAndUid(noteName,auService.getUid(token)).isEmpty())
        {
            throw new SameNoteNameFoundException();
        }
        Note newNote = new Note();
        newNote.setNoteName(noteName);
        newNote.setUid(auService.getUid(token));
        newNote.setCategory_id(resCat.findUncategorizedIdByUid(auService.getUid(token)));
        newNote.setFormat(format);
        Path storePath = storageService.store(file,String.valueOf(System.currentTimeMillis())+file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".")) );
        newNote.setLocation(storePath.toAbsolutePath().toString());
        newNote.setDescription(noteDescription);

        return res.save(newNote);
        /*final String uri = "http://localhost:8080/notes";

        Note uploadedNote=null;

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Note> request = new HttpEntity<>(newNote);
        uploadedNote = restTemplate.postForObject( uri, request, Note.class);


        return uploadedNote;*/
    }
}
