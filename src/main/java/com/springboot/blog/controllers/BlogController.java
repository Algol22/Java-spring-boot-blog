package com.springboot.blog.controllers;

import com.springboot.blog.domain.User;
//import com.springboot.blog.models.Comment;
import com.springboot.blog.models.Post;
//import com.springboot.blog.repo.CommentRepository;
import com.springboot.blog.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class BlogController {

    @Autowired
    private PostRepository postRepository;


    @GetMapping("/blog")
    public String blogMain(Model model){
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "blog-main";

    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model){
        return "blog-add";

    }


//    @PostMapping("/blog/{id}/comment")
//    public String blogPostAdd(@RequestParam Comment commented,@PathVariable(value= "id") long id, Model model){
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Date date = new Date();
//        Comment commentnew = new Comment(commented.getComment(), formatter.format(date).toString());
//        commentRepository.save(commentnew);
//        return "redirect:/blog";
//
//    }

    @PostMapping("/blog/add")
    public String blogPostAdd(@RequestParam String title, @RequestParam String anons, @RequestParam String full_text, Model model){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        Post post = new Post(title, anons, full_text, formatter.format(date).toString());
        postRepository.save(post);
        return "redirect:/blog";

    }

    @GetMapping("/403")
    public String error403() {
        return "/403";
    }


    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable(value= "id") long id, Model model){
        if(!postRepository.existsById(id)){
            return "redirect:/blog";
        }
        Optional <Post> post =  postRepository.findById(id);
        List<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);
        return "blog-details";

    }


    @GetMapping("/blog/{id}/edit")
    public String blogEdit(@PathVariable(value= "id") long id, Model model){ //Получаем id через url и получаем модель
        if(!postRepository.existsById(id)){
            return "redirect:/blog";
        }
        Optional <Post> post =  postRepository.findById(id);
        ArrayList <Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);
        return "blog-edit";

    }


//редактирование записей - обращаемся в >model 'post'>postrepository> interface 'postrepository'
    @PostMapping("/blog/{id}/edit")
    public String blogPostUpdate(@PathVariable(value="id") long id, @RequestParam String title, @RequestParam String anons, @RequestParam String full_text, Model model){
        Post post = postRepository.findById(id).orElseThrow();//если запись не найдена
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);
        postRepository.save(post);
        return "redirect:/blog";

    }


    @PostMapping("/blog/{id}/remove")
    public String blogPostDelete(@PathVariable(value="id") long id, Model model){
        Post post = postRepository.findById(id).orElseThrow();//если запись не найдена
        postRepository.delete(post);
        return "redirect:/blog";

    }


}
