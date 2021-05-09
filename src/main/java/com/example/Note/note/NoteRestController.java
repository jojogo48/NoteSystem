package com.example.Note.note;

import com.example.Note.category.Category;
import com.example.Note.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Transactional
public class NoteRestController {

    @Autowired
    private final NoteRepository res;
    @Autowired
    private final CategoryRepository resCat;

    public NoteRestController(NoteRepository res, CategoryRepository resCat) {
        this.res = res;
        this.resCat = resCat;
    }

    @GetMapping("/notes/{id}")
    Optional<Note> one(@PathVariable Long id){

        return res.findById(id);
    }

    @PostMapping("/notes")
    Note newNote(@RequestBody Note newNote) throws Exception
    {
        return res.save(newNote);
    }
    @GetMapping("/notes")
    List<Note> all()
    {
        return res.findAll();
    }

    @GetMapping("/notes/name/{name}")
    List<Note> searchName(@PathVariable(value="name") String name)
    {
        return  res.findByName(name);
    }

    @GetMapping("/notes/binaryname/{name}")
    List<Note> searchBinaryName(@PathVariable(value="name") String name)
    {
        return  res.findByBinaryName(name);
    }

    @GetMapping("/notes/categories")
    Map<String,List<Note>> listAllGroupByCategoryName()
    {
        Map<String,List<Note>> map = new HashMap<>();
        List<Category> noteCategory = resCat.findAll();

        for(int i=0;i<noteCategory.size();i++)
        {
            map.put(noteCategory.get(i).getCategoryName(),res.findByCategory(noteCategory.get(i).getCategoryName()));
        }
        return map;
    }

    @PostMapping("/notes/change")
    Map<String,String> changeCategory(@RequestBody Map<String, Object> payload)
    {
        Map<String,String> message = new HashMap<>();
        String category =(String)payload.get("category");
        ArrayList<String> notes = (ArrayList<String>) payload.get("notes");
        int cnt = 0;
        for(int i=0;i<notes.size();i++)
        {
            int value = res.setNoteCategory(notes.get(i),category);
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

    @GetMapping("/notes/category/{categoryName}")
    List<Note> listAllGroupByCategoryName(@PathVariable(value="categoryName") String name)
    {
        return res.findByCategory(name);
    }

    @GetMapping("test")
    List<Map<String,String>> test()
    {
        return res.joinTest();
    }


}
