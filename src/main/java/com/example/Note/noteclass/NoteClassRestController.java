package com.example.Note.noteclass;

import com.example.Note.noteclass.NoteClass;
import com.example.Note.noteclass.NoteClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoteClassRestController {

    @Autowired
    private final NoteClassRepository res;


    public NoteClassRestController(NoteClassRepository res) {
        this.res = res;
    }

    @GetMapping("/noteClass")
    CollectionModel<NoteClass> all()
    {
        return CollectionModel.of(res.findAll());
    }
}
