package com.example.Note.controller;

import com.example.Note.entity.Category;
import com.example.Note.repository.CategoryRepository;
import com.example.Note.service.AuthenticationService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CategoryRestController {
    private final AuthenticationService auService;

    @Autowired
    private final CategoryRepository res;

    public CategoryRestController(AuthenticationService auService, CategoryRepository res) {
        this.auService = auService;
        this.res = res;
    }

    @PostMapping("/noteCategory")
    Object all(@RequestBody Map<String,String> tmp)
    {
        return res.findByUid(auService.getUid(tmp.get("token")));
    }

    @PostMapping("/noteCategory/create")
    Object create(@RequestBody Map<String,String> tmp)
    {
        Category newNoteCategory = new Category(auService.getUid(tmp.get("token")),tmp.get("category_name"));
        return res.save(newNoteCategory);
    }

}
