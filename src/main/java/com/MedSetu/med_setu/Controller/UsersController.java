package com.MedSetu.med_setu.Controller;

import com.MedSetu.med_setu.Model.UsersEntity;
import com.MedSetu.med_setu.Services.UsersService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/activeuser")
    public UsersEntity fetchUser(Principal principal){
        UsersEntity user = usersService.fetchUser(principal.getName());
        return user;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UsersEntity userData) {
        UsersEntity encodedUser = userData;
        encodedUser.setPassword(passwordEncoder.encode(userData.getPassword()));
        Boolean create = usersService.createUser(encodedUser);
        if(create){

            return ResponseEntity.ok("User create with name "+userData.getName() + " email "+userData.getUsername() + " phone "+userData.getPhone());
        }
        else{
            return ResponseEntity.status(500).body("Data not saved!");
        }
    }
}
