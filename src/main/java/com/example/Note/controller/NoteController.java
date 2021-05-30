package com.example.Note.controller;

import com.example.Note.repository.CategoryRepository;
import com.example.Note.entity.Note;
import com.example.Note.repository.NoteRepository;
import com.example.Note.service.AuthenticationService;
import com.example.Note.service.NoteService;
import com.example.Note.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Transactional
public class NoteController {
    @Autowired
    private AuthenticationService auService;
    @Autowired
    private NoteService noteService;
    @Autowired
    private NoteRepository res;
    @Autowired
    private final CategoryRepository resCat;
    @Autowired
    private final StorageService strSer;

    public NoteController(AuthenticationService auService, NoteService noteService, NoteRepository res, CategoryRepository resCat, StorageService strSer) {
        this.auService = auService;
        this.noteService = noteService;
        this.res = res;
        this.resCat = resCat;
        this.strSer = strSer;
    }



    @PostMapping("/notes/{id}")
    Note one(@PathVariable Long id,@RequestBody Map<String,String> tmp) throws Exception {
        return noteService.getNoteByIdAndUid(id,auService.getUid(tmp.get("token")));
    }

    @PostMapping("/notes")
    List<Map<String, Object>> all(@RequestBody Map<String,String> tmp)
    {
        return noteService.getNotesByUid(auService.getUid(tmp.get("token")));
    }

    @PostMapping("/notes/name/{name}")
    List<Map<String,Object>> searchName(@PathVariable(value="name") String name, @RequestBody Map<String,String> tmp)
    {
        return noteService.getNotesByUid(auService.getUid(tmp.get("token")));
    }

    @PostMapping("/notes/binaryname/{name}")
    List<Map<String,Object>> searchBinaryName(@PathVariable(value="name") String name, @RequestBody Map<String,String> tmp)
    {
        return noteService.getNotesByBinaryName(name,auService.getUid(tmp.get("token")));
    }

    @PostMapping("/notes/name/{name}/{category}/{format}")
    List<Map<String,Object>> search(@PathVariable(value="name") String name,@PathVariable(value="category") String category,@PathVariable(value="format") String format, @RequestBody Map<String,String> tmp)
    {

        return noteService.getNotesByNameAndCategoryAndFormatAndUid(name,category,format, auService.getUid(tmp.get("token")));
    }

    @PostMapping("/notes/binaryname/{name}/{category}/{format}")
    List<Map<String,Object>> searchBinary(@PathVariable(value="name") String name,@PathVariable(value="category") String category,@PathVariable(value="format") String format, @RequestBody Map<String,String> tmp)
    {
        return noteService.getNotesByBinaryNameAndCategoryAndFormatAndUid(name,category,format, auService.getUid(tmp.get("token")));
    }


    @PostMapping("/notes/categories")
    Map<String,List<Note>> listAllNotesByCategoryName(@RequestBody Map<String,String> tmp)
    {
        return noteService.getNotesGroupedByCategoryName(auService.getUid(tmp.get("token")));
    }

    @PostMapping("/notes/change")
    Map<String,String> changeCategory(@RequestBody Map<String, Object> payload)
    {
        Map<String,String> message = new HashMap<>();
        Long uid = auService.getUid((String) payload.get("token"));
        Long category =Long.parseLong((String)payload.get("category"));
        ArrayList<String> notes = (ArrayList<String>) payload.get("notes");
        return noteService.setCategoryByCategoryIdAndNoteIdsAndUid(category,notes,uid);

    }

    @PostMapping("/notes/category/{categoryName}")
    List<Note> listAllGroupByCategoryName(@PathVariable(value="categoryName") String name,@RequestBody Map<String,String> tmp)
    {
        return noteService.getNotesByCategoryName(name,auService.getUid(tmp.get("token")));
    }

    @PostMapping("/notes/delete/{id}")
    void deleteNote(@PathVariable(value="id") String noteId,@RequestBody Map<String,String> tmp){

        Note deleteNote = noteService.getNoteByIdAndUid(Long.parseLong(noteId),auService.getUid(tmp.get("token")));
        strSer.deleteOnExit(deleteNote.getLocation());
        noteService.deleteNoteByIdAndUid(Long.parseLong(noteId),auService.getUid(tmp.get("token")));
    }

}
