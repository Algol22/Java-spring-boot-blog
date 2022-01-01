package com.springboot.blog.controllers;

import com.springboot.blog.domain.User;
//import com.springboot.blog.models.Comment;
import com.springboot.blog.models.Comment;
import com.springboot.blog.models.Favourites;
import com.springboot.blog.models.Post;
//import com.springboot.blog.repo.CommentRepository;
import com.springboot.blog.repo.CommentRepository;
import com.springboot.blog.repo.FavRepository;
import com.springboot.blog.repo.PostRepository;
import com.springboot.blog.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class BlogController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FavRepository favRepository;

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




    @PostMapping("/blog/{id}")
    public String add(
            @AuthenticationPrincipal User currentUser,
            @RequestParam String text,@PathVariable(value= "id") long id,
            Map<String, Object> model
    ){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        Post post = postRepository.findById(id).orElseThrow();//если запись не найдена
        Comment commentnew = new Comment(text, post, currentUser.getUsername(),formatter.format(date).toString() );
        commentRepository.save(commentnew);

        return "redirect:/blog/"+id;

    }





    @PostMapping("/blog/add")
    public String blogPostAdd(@RequestParam String title, @RequestParam String anons, @RequestParam String tag,@RequestParam String full_text, Model model){
        Date date = new Date();
        Post post = new Post(title, anons, full_text, tag, date);
        postRepository.save(post);
        return "redirect:/blog";

    }

    @PostMapping("/blog/search")
    public String blogSearch(@RequestParam String tag, Model model){
        if(tag.isEmpty()){
            Iterable<Post> posts = postRepository.findAll();
            model.addAttribute("posts", posts);
        }else {

            Iterable<Post> posts = postRepository.retrieveByTag(tag);

            if (!posts.iterator().hasNext()) {
                model.addAttribute("message", "Not found");
            } else {
                model.addAttribute("posts", posts);
            }
        }
        return "blog-main";
    }

    @GetMapping("/403")
    public String error403() {
        return "/403";
    }


    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable(value= "id") long id ,Model model){
        if(!postRepository.existsById(id)){
            return "redirect:/blog";
        }

        Optional <Post> post =  postRepository.findById(id);
        List<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);

        Set<Comment> comments = post.get().getComments();
        if(comments.stream().toArray().length>0) {
            model.addAttribute("comments", comments);
            model.addAttribute("message", "Comments:");
        }else{
            model.addAttribute("message", "");
        }
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
    public String blogPostUpdate(@PathVariable(value="id") long id, @RequestParam String title, @RequestParam String anons, @RequestParam String tag, @RequestParam String full_text, Model model){
        Post post = postRepository.findById(id).orElseThrow();//если запись не найдена
        post.setTitle(title);
        post.setAnons(anons);
        post.setTag(tag);
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

    @PostMapping("/blog/{intg}/{id}/removecomment")
    public String blogCommentDelete(@PathVariable(value="id") long id, @PathVariable(value="intg") long intg, Model model){
       Comment comment = commentRepository.findById(id).orElseThrow();
        commentRepository.delete(comment);
        return "redirect:/blog/{intg}";

    }



    @PostMapping("/blog")
    public String sortPosts(@RequestParam(required = false) String sortBy ,@RequestParam(required = false) Long id, Model model) {

        if(id!=null){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User user = userRepo.findByUsername(userName);
            Favourites favourite = new Favourites(user.getId(), id);
            favRepository.save(favourite);
            Iterable<Post> posts = postRepository.findAll();
            model.addAttribute("posts", posts);
            return "redirect:/blog";
        }


        if(sortBy.equals("desc")) {
            Iterable<Post> posts = postRepository.sortByDescDate();
            model.addAttribute("posts", posts);

        }else {
            Iterable<Post> posts = postRepository.sortByAscDate();
            model.addAttribute("posts", posts);
        }
        return "blog-main";
    }


}
