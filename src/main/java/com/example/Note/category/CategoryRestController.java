package com.example.Note.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class CategoryRestController {

    @Autowired
    private final CategoryRepository res;

    public CategoryRestController(CategoryRepository res) {
        this.res = res;
    }

    @GetMapping("/noteCategory")
    List<Category> all()
    {
        return res.findAll();
    }

    @PostMapping("/noteCategory/create")
    Category create(@RequestBody Map<String,String> tmp)
    {
        Category newNoteCategory = new Category();
        newNoteCategory.setCategoryName(tmp.get("category_name"));
        return res.save(newNoteCategory);
    }

}
