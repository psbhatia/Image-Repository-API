package com.example.image.repo.api;

import com.example.image.repo.Constants;
import com.example.image.repo.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/login")
public class UserController {

    @PostMapping
    public Map<String, String> login(@RequestParam(value = "email") String email,
                                                     @RequestParam(value = "password") String password){
        User user = new User(email, password);
        Map<String, String> response = new HashMap<>();
        if (authenticateUser(user)){
            return generateJWTtoken(user);
        }
        response.put("authentication", "failed");
        return response;

    }

    private Map<String, String> generateJWTtoken(User user){
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim("email", user.getEmail())
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }

    private boolean authenticateUser(User user){
        if (user.getEmail().equals("admin@imagerepo.com") && user.getPassword().equals("password")){
            return true;
        }
        return false;
    }
}
