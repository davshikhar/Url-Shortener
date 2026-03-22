package com.example.demo.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class ShortUrlGenerator {
    private static final String BASE62_Chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int Code_length = 6;

    private final SecureRandom secureRandom;

    public ShortUrlGenerator(){
        this.secureRandom=new SecureRandom();
    }

    /// we use SecureRandom because it is very secure and no one can predict the random code.

    public String generate(){
        StringBuilder code = new StringBuilder(Code_length);

        for(int i=0;i<Code_length;i++){
            int random = secureRandom.nextInt(BASE62_Chars.length());
            code.append(BASE62_Chars.charAt(random));

        }
        return code.toString();
    }
}
