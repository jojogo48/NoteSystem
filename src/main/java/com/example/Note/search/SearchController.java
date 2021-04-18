package com.example.Note.search;

import com.example.Note.note.Note;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class SearchController {


    @GetMapping("/search")
    public String search(Model model, @RequestParam(required = false) String keyword,@RequestParam(required = false) String diffCase)
    {

        if(keyword == null || keyword.isEmpty())
            return "search";

        String uri = "http://localhost:8080/notes/binaryname/"+keyword;

        if(diffCase==null)
        {
            diffCase="off";
            uri = "http://localhost:8080/notes/name/"+keyword;
        }


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Note[]> searchResult= restTemplate.getForEntity( uri,Note[].class);
        Note[] noteArray = searchResult.getBody();

        model.addAttribute("notes",noteArray);
        model.addAttribute("diffCase",diffCase);

        return "search";
    }
}
