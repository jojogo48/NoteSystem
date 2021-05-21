package com.example.Note.component;
import java.util.UUID;
public class Constant {
    public static final String JWT_ID = UUID.randomUUID().toString();

    public static final String JWT_SECRET = "woyebuzhidaoxiediansha";
    public static final int JWT_TTL = 60*60*1000;  //millisecond
}