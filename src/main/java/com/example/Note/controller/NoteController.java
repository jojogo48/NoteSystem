package com.example.Note.controller;

import com.example.Note.entity.Category;
import com.example.Note.exception.NoAccessToFileException;
import com.example.Note.repository.CategoryRepository;
import com.example.Note.entity.Note;
import com.example.Note.repository.NoteRepository;
import com.example.Note.service.AuthenticationService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@Transactional
public class NoteController {
    @Autowired
    private AuthenticationService auService;
    @Autowired
    private final NoteRepository res;
    @Autowired
    private final CategoryRepository resCat;


    public NoteController(AuthenticationService auService, NoteRepository res, CategoryRepository resCat) {
        this.auService = auService;
        this.res = res;
        this.resCat = resCat;
    }



    @PostMapping("/notes/{id}")
    Optional<Note> one(@PathVariable Long id,@RequestBody Map<String,String> tmp) {
        if(!auService.isNoteBelongToUser(id,auService.getUid(tmp.get("token")))){
            throw new NoAccessToFileException();
        }
        return res.findById(id);
    }

    /*@PostMapping("/notes")
    Note newNote(@RequestBody Map<String,String> tmp) throws Exception
    {
        Note newNote = new Note();
        newNote.setCategory_id(0L);
        newNote.setUid(auService.getUid(tmp.get("token")));
        newNote.setNoteName(tmp.get("noteName"));
        newNote.setFormat(tmp.get("format"));
        newNote.set
        return res.save(newNote);
    }*/
    @PostMapping("/notes")
    List<Map<String, Object>> all(@RequestBody Map<String,String> tmp)
    {
        return res.findNoteByUid(auService.getUid(tmp.get("token")));
    }

    @PostMapping("/notes/name/{name}")
    List<Note> searchName(@PathVariable(value="name") String name, @RequestBody Map<String,String> tmp)
    {

        return  res.findByNameAndUid(name,auService.getUid(tmp.get("token")));
    }

    @PostMapping("/notes/binaryname/{name}")
    List<Note> searchBinaryName(@PathVariable(value="name") String name, @RequestBody Map<String,String> tmp)
    {

        return  res.findByBinaryNameAndUid(name,auService.getUid(tmp.get("token")));
    }

    @PostMapping("/notes/name/{name}/{category}/{format}")
    List<Note> search(@PathVariable(value="name") String name,@PathVariable(value="category") String category,@PathVariable(value="format") String format, @RequestBody Map<String,String> tmp)
    {

        return  res.findByNameAndCategoryAndFormatAndUid(category,name,format,auService.getUid(tmp.get("token")));
    }

    @PostMapping("/notes/binaryname/{name}/{category}/{format}")
    List<Note> searchBinary(@PathVariable(value="name") String name,@PathVariable(value="category") String category,@PathVariable(value="format") String format, @RequestBody Map<String,String> tmp)
    {

        return  res.findByBinaryNameAndCategoryAndFormatAndUid(category,name,format,auService.getUid(tmp.get("token")));
    }


    @PostMapping("/notes/categories")
    Map<String,List<Note>> listAllNotesByCategoryName(@RequestBody Map<String,String> tmp)
    {

        Map<String,List<Note>> map = new HashMap<>();
        List<Category> noteCategory = resCat.findByUid(auService.getUid(tmp.get("token")));

        for(int i=0;i<noteCategory.size();i++)
        {
            map.put(noteCategory.get(i).getCategoryName(),res.findByCategoryAndUid(noteCategory.get(i).getCategoryName(),auService.getUid(tmp.get("token"))));
        }
        return map;
    }

    @PostMapping("/notes/change")
    Map<String,String> changeCategory(@RequestBody Map<String, Object> payload)
    {

        Map<String,String> message = new HashMap<>();
        Long category =Long.parseLong((String)payload.get("category"));
        ArrayList<String> notes = (ArrayList<String>) payload.get("notes");
        int cnt = 0;
        for(int i=0;i<notes.size();i++)
        {
            int value = res.setNoteCategoryAndUid(notes.get(i),category,auService.getUid((String) payload.get("token")));
            cnt += value;
        }
        if(cnt == notes.size())
        {
            message.put("message","successful!!");
        }else{
            message.put("error","Not all change successfully!!");
        }
        return message;
    }

    @PostMapping("/notes/category/{categoryName}")
    List<Note> listAllGroupByCategoryName(@PathVariable(value="categoryName") String name,@RequestBody Map<String,String> tmp)
    {
        return res.findByCategoryAndUid(name,auService.getUid(tmp.get("token")));
    }


}
