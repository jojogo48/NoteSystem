package com.example.Note.service;

import com.example.Note.component.Constant;
import com.example.Note.component.JwtTokenUtil;
import com.example.Note.entity.User;
import com.example.Note.repository.CategoryRepository;
import com.example.Note.repository.NoteRepository;
import com.example.Note.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private NoteRepository noteRepo;


    public Map<String,Object> checkUser(String account, String password) throws JwtException {
        JwtTokenUtil util = new JwtTokenUtil();
        Map<String, Object> map = new HashMap<>();
        User findUser= userRepo.findByName(account,password);
        if (findUser==null) {
            map.put("success", false);
            map.put("message", "帳號或密碼錯誤");

            return map;
        }
        map.put("success", true);
        map.put("message", "登錄成功");
        map.put("token",util.createJWT(Constant.JWT_ID, "JoJo", findUser.getId().toString(), Constant.JWT_TTL));
        return map;
    }
    /*public boolean isLogin(String jwt)
    {
        JwtTokenUtil util = new JwtTokenUtil();
        util.
        if(session.getAttribute("uid") == null)
        {
            return false;
        }else{
            return true;
        }
    }*/

    public boolean checkToken(String jwt)
    {
        try {
            JwtTokenUtil util = new JwtTokenUtil();
            Claims c = util.parseJWT(jwt);
        }catch (Exception ex){
            return false;
        }
        return true;
    }

    public Long getUid(String jwt)
    {
        JwtTokenUtil util = new JwtTokenUtil();
        Claims c = util.parseJWT(jwt);
        return Long.parseLong(c.getSubject(),10);
    }

    public boolean isNoteBelongToUser(Long noteId,Long uid){

        List<Map<String,Object>> noteIds= noteRepo.findNoteByUid(uid);
        for(int i=0;i<noteIds.size();i++)
        {
            if(noteIds.get(i).get("id").toString().equals(noteId.toString()))
            {
                return true;
            }
        }
        return false;
    }

}
