package com.example.Note.note;

import com.example.Note.note.Note;
import com.example.Note.note.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class NoteRestController {

    @Autowired
    private final NoteRepository res;


    public NoteRestController(NoteRepository res) {
        this.res = res;
    }

    @GetMapping("/notes/{id}")
    Optional<Note> one(@PathVariable Long id){

        return res.findById(id);
    }

    @PostMapping("/notes")
    Note newNote(@RequestBody Note newNote)
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

    @GetMapping("test")
    List<Map<String,String>> test()
    {
        return res.joinTest();
    }
}
