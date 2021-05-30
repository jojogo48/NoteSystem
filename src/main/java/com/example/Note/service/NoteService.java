package com.example.Note.service;

import com.example.Note.entity.Category;
import com.example.Note.entity.Note;
import com.example.Note.exception.NoAccessToFileException;
import com.example.Note.exception.SameNoteNameFoundException;
import com.example.Note.repository.CategoryRepository;
import com.example.Note.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.Note.exception.NoteNotFoundException;

import java.net.URLEncoder;
import java.util.*;

@Service
public class NoteService {
    @Autowired
    private final NoteRepository res;
    @Autowired
    private final CategoryRepository resCat;
    @Autowired
    private AuthenticationService auService;

    public NoteService(NoteRepository res, CategoryRepository resCat, AuthenticationService auService) {
        this.res = res;
        this.resCat = resCat;
        this.auService = auService;
    }

    public Note getNoteByIdAndUid(Long id,Long uid) {
        auService.isNoteBelongToUser(id,uid);
        Optional<Note> uploadedNoteOpt = res.findById(id);

        if (!uploadedNoteOpt.isPresent()) {
            throw new NoteNotFoundException();
        }
        return uploadedNoteOpt.get();
    }

    public List<Map<String, Object>> getNotesByUid(Long uid)
    {
        return res.findNoteByUid(uid);
    }

    public List<Map<String,Object>> getNotesByName(String name,Long uid)
    {
        return  res.findByNameAndUid(name,uid);
    }

    public List<Map<String,Object>> getNotesByBinaryName(String name,Long uid)
    {
        return  res.findByBinaryNameAndUid(name,uid);
    }

    public String getHeaderByFormat(String format)
    {
        if(format.equals("text"))
        {
            return "text/plain;charset=utf-8";

        }
        if(format.equals("pdf"))
        {
            return "application/pdf;charset=utf-8";
        }
        if(format.equals("picture"))
        {
            return "image/png;charset=utf-8";
        }
        if(format.equals("html"))
        {
            return "text/html;charset=utf-8";
        }

        return "application/octet-stream;charset=utf-8";
    }

    public List<Map<String,Object>> getNotesByNameAndCategoryAndFormatAndUid(String name,String category,String format,Long uid)
    {
        //Todo 需要重構不要有IF
        if(category.equals("all") && !format.equals("all"))
        {
            return  res.findByNameAndFormatAndUid(name,format,uid);
        }else if(!category.equals("all") && format.equals("all"))
        {
            return  res.findByNameAndCategoryAndUid(name,category,uid);
        }else if(category.equals("all") && format.equals("all")){
            return  res.findByNameAndUid(name,uid);
        }
        return  res.findByNameAndCategoryAndFormatAndUid(category,name,format,uid);
    }

    public List<Map<String,Object>> getNotesByBinaryNameAndCategoryAndFormatAndUid(String name,String category,String format,Long uid)
    {
        if(category.equals("all") && !format.equals("all"))
        {
            return  res.findByBinaryNameAndFormatAndUid(name,format,uid);
        }else if(!category.equals("all") && format.equals("all"))
        {
            return  res.findByBinaryNameAndCategoryAndUid(name,category,uid);
        }else if(category.equals("all") && format.equals("all")){
            return  res.findByBinaryNameAndUid(name,uid);
        }
        return  res.findByBinaryNameAndCategoryAndFormatAndUid(category,name,format,uid);
    }


    public Map<String,List<Note>> getNotesGroupedByCategoryName(Long uid)
    {

        Map<String,List<Note>> map = new HashMap<>();
        List<Category> noteCategory = resCat.findByUid(uid);
        for(int i=0;i<noteCategory.size();i++)
        {
            map.put(noteCategory.get(i).getCategoryName(),res.findByCategoryAndUid(noteCategory.get(i).getCategoryName(),uid));
        }

        return map;
    }

    public Map<String,String> setCategoryByCategoryIdAndNoteIdsAndUid(Long category,ArrayList<String> notes,Long uid)
    {
        Map<String,String> message = new HashMap<>();

        int cnt = 0;
        for(int i=0;i<notes.size();i++)
        {
            int value = res.setNoteCategoryAndUid(notes.get(i),category,uid);
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

    public List<Note> getNotesByCategoryName(String name, Long uid)
    {
        return res.findByCategoryAndUid(name,uid);
    }

    public boolean checkHasSameNoteName(String noteName,Long uid){
        if(!res.findByNameAndUid(noteName,uid).isEmpty())
        {
            throw new SameNoteNameFoundException();
        }
        return false;
    }

    public void deleteNoteByIdAndUid(Long id,Long uid){

        res.deleteByIdAndUid(id.toString(),uid);
    }
}
