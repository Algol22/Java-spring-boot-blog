package com.springboot.blog.controllers;

import com.springboot.blog.FileUpload.FileUploadUtil;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
    public String blogMain(Model model, Principal user, @PageableDefault(sort=("id"),direction= Sort.Direction.DESC) Pageable pageable) {

        Page <Post> page;
        page = (Page<Post>) postRepository.findAll(pageable);


        //favourites
        if (user != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User usercheck = userRepo.findByUsername(userName);
            Iterable<Long> favourites = favRepository.findFavourites(usercheck.getId());
            List<Long> resultFavourites = new ArrayList<>();
            favourites.forEach(resultFavourites::add);
            model.addAttribute("favourites", favourites);
        }

        model.addAttribute("posts", page);
        model.addAttribute("url", "/blog");


        return "blog-main";
    }




    @GetMapping("/blog/add")
    public String blogAdd(Model model, Principal user) {
        if (user != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {

                return "blog-add";
            }
        }
        return "blog-main";
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
        Comment commentnew = new Comment(text, post, currentUser.getUsername(),date);
      commentRepository.save(commentnew);

            int counter = commentRepository.countComments(id);
            postRepository.updateComments(id, counter);


        return "redirect:/blog/"+id;

    }





    @PostMapping("/blog/add")
    public String blogPostAdd(@RequestParam String title, @RequestParam String anons, @RequestParam String tag, @RequestParam String full_text, @RequestParam String photoUrl, @RequestParam("image") MultipartFile multipartFile, Model model){
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

                Date date = new Date();
                Post post = new Post(title, anons, full_text, tag, date, 0, fileName, photoUrl);
                postRepository.save(post);

                String uploadDir = "src/main/webapp/WEB-INF/images/" + post.getId();

                try {
                    FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

        return "redirect:/blog";

    }




    @PostMapping("/blog/addphoto")
    public String blogPostAddPhoto(@RequestParam("image") MultipartFile multipartFile, @RequestParam("id") Long id, Model model){
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        postRepository.updatePhoto(id,fileName);
        String uploadDir = "src/main/webapp/WEB-INF/images/" + id;

        try {
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/blog";

    }







    @GetMapping("/blog/search/{tag}")
    public String blogSearch(@PathVariable String tag, Model model, Principal user, @PageableDefault(sort=("id"),direction= Sort.Direction.DESC) Pageable pageable){
        if (user != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User usercheck = userRepo.findByUsername(userName);
            Iterable<Long> favourites = favRepository.findFavourites(usercheck.getId());
            List<Long> resultFavourites = new ArrayList<>();
            favourites.forEach(resultFavourites::add);
            model.addAttribute("favourites", favourites);
        }

        Page <Post> page;

        if(tag.isEmpty()){
            page = (Page<Post>)  postRepository.findAll();
            model.addAttribute("posts", page);


        }else {

            page = (Page<Post>) postRepository.retrieveByTag(tag, pageable);

            if (!page.iterator().hasNext()) {
                model.addAttribute("message", "Not found");
                model.addAttribute("posts", page);
            } else {
                model.addAttribute("posts", page);
            }
        }

        return "blog-main";
    }


    @PostMapping("/blog/search")
    public String blogSearchPost(@RequestParam String tag, Model model, Principal user, @PageableDefault(sort=("id"),direction= Sort.Direction.DESC) Pageable pageable){

        if (user != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User usercheck = userRepo.findByUsername(userName);
            Iterable<Long> favourites = favRepository.findFavourites(usercheck.getId());
            List<Long> resultFavourites = new ArrayList<>();
            favourites.forEach(resultFavourites::add);
            model.addAttribute("favourites", favourites);
        }

        Page <Post> page;

        if(tag.isEmpty()){
            page = (Page<Post>) postRepository.findAll();
            model.addAttribute("posts", page);
        }else {

            page = (Page<Post>)  postRepository.retrieveByTag(tag, pageable);

            if (!page.iterator().hasNext()) {
                model.addAttribute("message", "Not found");
                model.addAttribute("posts", page);

            } else {
                model.addAttribute("posts", page);
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
    public String blogEdit(@PathVariable(value= "id") long id, Model model, Principal user){ //Получаем id через url и получаем модель



        if(!postRepository.existsById(id)){
            return "redirect:/blog";
        }

        if (user != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {


                Optional<Post> post = postRepository.findById(id);
                ArrayList<Post> res = new ArrayList<>();
                post.ifPresent(res::add);
                model.addAttribute("post", res);
                return "blog-edit";
            }
        }
        return "redirect:/blog";
    }


//редактирование записей - обращаемся в >model 'post'>postrepository> interface 'postrepository'
    @PostMapping("/blog/{id}/edit")
    public String blogPostUpdate(@PathVariable(value="id") long id, @RequestParam String title, @RequestParam String anons, @RequestParam String tag, @RequestParam String full_text,@RequestParam String photoUrl, Model model){
        Post post = postRepository.findById(id).orElseThrow();//если запись не найдена
        post.setTitle(title);
        post.setAnons(anons);
        post.setTag(tag);
        post.setFull_text(full_text);
        post.setPhotoUrl(photoUrl);
        postRepository.save(post);
        return "redirect:/blog";

    }


    @PostMapping("/blog/{id}/remove")
    public String blogPostDelete(@PathVariable(value="id") long id, Model model){
        Post post = postRepository.findById(id).orElseThrow();//если запись не найдена
        postRepository.delete(post);

        try {
            Files.deleteIfExists(Path.of("src/main/webapp/WEB-INF/images/" + id + "/" + postRepository.findPhotoById(id) + "/"));
            FileUtils.deleteDirectory(new File("src/main/webapp/WEB-INF/images/"+id+"/"));
        }
        catch(Exception e)
        {
            System.out.println("Failed to Delete image !!");
        }
        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/removephoto")
    public String blogPhotoDelete(@PathVariable(value="id") long id, Model model){

        postRepository.deletePhoto(id);
        try {
            Files.deleteIfExists(Path.of("src/main/webapp/WEB-INF/images/" + id + "/" + postRepository.findPhotoById(id) + "/"));
            FileUtils.deleteDirectory(new File("src/main/webapp/WEB-INF/images/"+id+"/"));

        }
        catch(Exception e)
        {
            System.out.println("Failed to Delete image !!");
        }
        return "redirect:/blog";
    }


    @PostMapping("/blog/{intg}/{id}/removecomment")
    public String blogCommentDelete(@PathVariable(value="id") long id, @PathVariable(value="intg") long intg, Model model){
       Comment comment = commentRepository.findById(id).orElseThrow();
        commentRepository.delete(comment);
        return "redirect:/blog/{intg}";

    }



    @PostMapping("/blog")
    public String sortPosts(@RequestParam(required = false) String sortBy ,@RequestParam(required = false) Long id, Model model, Principal user, @PageableDefault(sort=("id"),direction= Sort.Direction.ASC) Pageable pageable) {

        if (user != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User usercheck = userRepo.findByUsername(userName);
            Iterable<Long> favourites = favRepository.findFavourites(usercheck.getId());
            List<Long> resultFavourites = new ArrayList<>();
            favourites.forEach(resultFavourites::add);
            model.addAttribute("favourites", favourites);
        }


        if(id!=null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User useraddFavourites = userRepo.findByUsername(userName);
            Favourites favourite = new Favourites(useraddFavourites.getId(), postRepository.findById(id).orElseThrow());
            favRepository.save(favourite);
            Iterable<Post> posts = postRepository.findAll();
            model.addAttribute("posts", posts);
            return "redirect:/blog";
        }
        Page <Post> page;

        if(sortBy.equals("desc")) {
            page = (Page<Post>)postRepository.sortByDescDate(pageable);
            model.addAttribute("posts", page);

        }else {
            page = (Page<Post>) postRepository.sortByAscDate(pageable);
            model.addAttribute("posts", page);
        }
        return "blog-main";
    }


}
