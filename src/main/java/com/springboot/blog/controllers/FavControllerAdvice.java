package com.springboot.blog.controllers;

import com.springboot.blog.domain.User;
import com.springboot.blog.models.Post;
import com.springboot.blog.repo.FavRepository;
import com.springboot.blog.repo.PostRepository;
import com.springboot.blog.repo.UserRepo;
import org.apache.catalina.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class FavControllerAdvice {


    @Autowired
    UserRepo userRepo;
    @Autowired
    FavRepository favRepository;
    @Autowired
    PostRepository postRepository;

    @ModelAttribute
    public void addBugetToModel(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String userName = authentication.getName();
            User user = userRepo.findByUsername(userName);
            List<Long> countFav = favRepository.countFavourites(user.getId());
            if(countFav.get(0)<=0){
                model.addAttribute("messageFav", "You have no favourites");
            }

            model.addAttribute("counter", countFav.get(0));
            Iterable<Post> posts = postRepository.findAll();
            model.addAttribute("posts", posts);
        }
    }
}