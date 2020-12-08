package com.rawatdivyanshu.userregistrationsystem.Controller;

import com.rawatdivyanshu.userregistrationsystem.Constants;
import com.rawatdivyanshu.userregistrationsystem.Model.User;
import com.rawatdivyanshu.userregistrationsystem.Service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping({"/register","/register/","register"})
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
        user = userService.registerUser(user);
        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }

    @PostMapping({"/login", "login", "/login/"})
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, Object> userMap) {
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        User user = userService.validateUser(email, password);
        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }

    private Map<String, String> generateJWTToken(User user) {
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim("user_id", user.getUser_id())
                .claim("email", user.getEmail())
                .claim("first_name", user.getFirst_name())
                .claim("last_name", user.getLast_name())
                .compact();

        Map<String, String> map = new HashMap<>();
        map.put("token",token);
        return map;
    }

    @GetMapping({"/get-user","get-user","/get-user/"})
    public ResponseEntity<Map<String, Object>> getUserData(HttpServletRequest request) {
        Long userId = Long.parseLong(request.getAttribute("user_id").toString());
        User user = userService.getUserById(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("User_Details",user);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
