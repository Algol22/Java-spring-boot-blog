package com.springboot.blog.controllers;


import com.springboot.blog.domain.User;
import com.springboot.blog.models.Comment;
import com.springboot.blog.models.Favourites;
import com.springboot.blog.models.Post;
import com.springboot.blog.repo.CommentRepository;
import com.springboot.blog.repo.FavRepository;
import com.springboot.blog.repo.PostRepository;
import com.springboot.blog.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class FavouritesController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FavRepository favRepository;

    @PostMapping("/blog/fav/add")
    public String blogFavAdd(@RequestParam Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userRepo.findByUsername(userName);
        Favourites favourite = new Favourites(user.getId(), postRepository.findById(id).orElseThrow());
        favRepository.save(favourite);
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "blog-main";
    }



    @GetMapping("/favourites")
    public String blogFavpage(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userRepo.findByUsername(userName);
        Iterable<Post> posts = postRepository.findByPostId(user.getId());
        model.addAttribute("posts", posts );

        return "favourites";
    }


    @PostMapping("/blog/fav/remove")
    public String removeFav(@RequestParam Long id, Model model) {
    favRepository.deleteFav(id);
    postRepository.updateFav(id,false);
    return "redirect:/favourites";
    }




}
