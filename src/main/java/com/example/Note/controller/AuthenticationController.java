package com.example.Note.controller;
import com.example.Note.service.AuthenticationService;
import io.jsonwebtoken.JwtException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthenticationController {
    private final AuthenticationService auService;

    public AuthenticationController(AuthenticationService auService) {
        this.auService = auService;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String,String> tmp){
        return auService.checkUser(tmp.get("account"),tmp.get("password"));
    };

    @PostMapping("/checkToken")
    public Map<String, Object> checkToken(@RequestBody Map<String,String> tmp){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("successful", auService.checkToken(tmp.get("token")));
        return map;
    };


}
