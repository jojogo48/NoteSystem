package com.example.Note.controller;
import com.example.Note.entity.Note;
import com.example.Note.exception.NoAccessToFileException;
import com.example.Note.repository.CategoryRepository;
import com.example.Note.service.AuthenticationService;
import com.example.Note.service.NoteService;
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

    @Autowired
    private final NoteService noteService;

    public FileUploadController(AuthenticationService auService, StorageService storageService, NoteRepository res, CategoryRepository resCat, NoteService noteService) {
        this.auService = auService;
        this.storageService = storageService;
        this.res = res;
        this.resCat = resCat;
        this.noteService = noteService;
    }

    @Autowired
    public FileUploadController(StorageService storageService, NoteRepository res, CategoryRepository resCat, NoteService noteService) {
        this.storageService = storageService;

        this.res = res;
        this.resCat = resCat;
        this.noteService = noteService;
    }


    @GetMapping("/files/download/{token:.+}/{noteId:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String noteId,@PathVariable String token) throws Exception {


        Note uploadedNote = noteService.getNoteByIdAndUid(Long.parseLong(noteId),auService.getUid(token));
        Resource file = storageService.loadAsResource(uploadedNote.getLocation());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename*=UTF-8''" + URLEncoder.encode(file.getFilename(), "UTF-8")).header(HttpHeaders.CONTENT_TYPE,
                "application/octet-stream;charset=utf-8").body(file);
        //Todo charset不一定 utf-8 可以之後自己偵測
        //Todo 對於type 可以用工具自己偵測
    }

    @GetMapping("/files/{token:.+}/{noteId:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String noteId,@PathVariable String token) throws Exception {


        Note uploadedNote = noteService.getNoteByIdAndUid(Long.parseLong(noteId),auService.getUid(token));
        Resource file = storageService.loadAsResource(uploadedNote.getLocation());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "inline;filename*=UTF-8''" + URLEncoder.encode(file.getFilename(), "UTF-8")).header(HttpHeaders.CONTENT_TYPE,
                noteService.getHeaderByFormat(uploadedNote.getFormat())).body(file);
    }


    @PostMapping("/upload")
    public Note  handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("noteName") String noteName,
                                   @RequestParam("format") String format,
                                   @RequestParam("noteDescription") String noteDescription,
                                   @RequestParam("token") String token,
                                   RedirectAttributes redirectAttributes) throws IOException,SameNoteNameFoundException {

        noteService.checkHasSameNoteName(noteName,auService.getUid(token));
        Note newNote = new Note();
        newNote.setNoteName(noteName);
        newNote.setUid(auService.getUid(token));
        newNote.setCategory_id(resCat.findUncategorizedIdByUid(auService.getUid(token)));
        newNote.setFormat(format);
        Path storePath = storageService.store(file,String.valueOf(System.currentTimeMillis())+file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".")) );
        newNote.setLocation(storePath.toAbsolutePath().toString());
        newNote.setDescription(noteDescription);

        return res.save(newNote);
    }

}
