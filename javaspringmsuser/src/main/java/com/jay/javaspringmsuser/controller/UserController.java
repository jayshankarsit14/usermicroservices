package com.jay.javaspringmsuser.controller;

import com.jay.javaspringmsuser.dto.LoginRequestDTO;
import com.jay.javaspringmsuser.dto.LogoutRequestDTO;
import com.jay.javaspringmsuser.dto.SignUpRequestDTO;
import com.jay.javaspringmsuser.dto.UserDto;
import com.jay.javaspringmsuser.models.Token;
import com.jay.javaspringmsuser.models.User;
import com.jay.javaspringmsuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Token login(@RequestBody LoginRequestDTO loginRequestDTO) {
        //check email and pass in db if yes then return user else throw some error

        return userService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
    }

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignUpRequestDTO request) {
        //no need hashpassword
        //just store in db
        //no need user verification
        String email = request.getEmail();
        String password = request.getPassword();
        String name = request.getName();
        return UserDto.from(userService.signUp(name, email, password));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logOut(@RequestBody LogoutRequestDTO request) {
        //delete token if exists->200
        //if doesn't exist give a 404
        userService.logOut(request.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/validate/{token}")
    public User validateToken(@PathVariable @NonNull String token){
        System.out.println("validated");
        return userService.validateToken(token);
    }
}
