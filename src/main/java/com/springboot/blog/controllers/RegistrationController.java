package com.springboot.blog.controllers;

import com.springboot.blog.domain.Role;
import com.springboot.blog.domain.User;
import com.springboot.blog.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String registration(Map<String, Object> model) {
       User user = new User();
        model.put("user", user);
        return "registration";
    }



    @PostMapping("/registration")
    public String addUser(@Valid User user,  BindingResult bindingResult, Map<String, Object> model) {

        if(bindingResult.hasErrors())
            return "registration";

        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.put("message", "User exists!");
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.ADMIN));
        userRepo.save(user);
        model.put("message", "Welcome " + user.getUsername() + ". Please log in");
        return "login";
    }
}
