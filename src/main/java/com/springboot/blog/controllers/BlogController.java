package com.springboot.blog.controllers;

import com.springboot.blog.FileUpload.FileUploadUtil;
import org.apache.tomcat.util.http.fileupload.FileUtils;
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
    public String blogMain(Model model, Principal user) {

        Iterable<Post> posts = postRepository.findAllPosts();
        List<Post> resultPosts = new ArrayList<>();
        posts.forEach(resultPosts::add);

        if (user != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User usercheck = userRepo.findByUsername(userName);
            Iterable<Long> favourites = favRepository.findFavourites(usercheck.getId());
            List<Long> resultFavourites = new ArrayList<>();
            favourites.forEach(resultFavourites::add);
            model.addAttribute("favourites", favourites);
        }


        int totalPosts = IterableUtils.size(posts);
        int numberOfPosts = 10;

        model.addAttribute("posts", resultPosts);


        if (totalPosts > numberOfPosts) {
            model.addAttribute("currentpage", 1);
            model.addAttribute("nextpage", 2);
        }
        return "blog-main";
    }


    @GetMapping("/blog/page/{pagenumber}")
    public String blogMain(@PathVariable int pagenumber, Model model, Principal user) {
        Iterable<Post> posts = postRepository.findAllPosts();
        int totalPosts = IterableUtils.size(posts);
        int pages = 0;
        int numberOfPosts = 10;
        int nextpage = pagenumber + 1;
        if (totalPosts < numberOfPosts) {
            nextpage = 0;
        }

        if (totalPosts > numberOfPosts) {
            pages = (int) Math.round((totalPosts / numberOfPosts) + 0.5);
        }
        List<Post> result = new ArrayList<>();
        posts.forEach(result::add);

        if (pagenumber < 2) {
            result.subList(numberOfPosts * pagenumber, result.size()).clear();

        } else if ((pagenumber * numberOfPosts) > totalPosts) {
            result.subList(0, numberOfPosts * (pagenumber - 1)).clear();
            nextpage = nextpage - 1;

        } else if ((pagenumber * numberOfPosts) < totalPosts) {
            result.subList(0, (pagenumber - 1) * numberOfPosts).clear();
            result.subList(0, totalPosts - (pagenumber * numberOfPosts)).clear();
        }


        if (user != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User usercheck = userRepo.findByUsername(userName);
            Iterable<Long> favourites = favRepository.findFavourites(usercheck.getId());
            List<Long> resultFavourites = new ArrayList<>();
            favourites.forEach(resultFavourites::add);
            model.addAttribute("favourites", favourites);
        }

        model.addAttribute("posts", result);
        model.addAttribute("firstpage", 1);
        model.addAttribute("nextpage", nextpage);
        model.addAttribute("previouspage", pagenumber - 1);
        model.addAttribute("currentpage", pagenumber);
        model.addAttribute("lastpage", pages);
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
    public String blogPostAdd(@RequestParam String title, @RequestParam String anons, @RequestParam String tag, @RequestParam String full_text, @RequestParam("image") MultipartFile multipartFile, Model model){
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());




                Date date = new Date();
                Post post = new Post(title, anons, full_text, tag, date, 0, fileName);
                postRepository.save(post);

                String uploadDir = "src/main/webapp/WEB-INF/images/" + post.getId();

                try {
                    FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

        return "redirect:/blog";

    }

    @GetMapping("/blog/search/{tag}")
    public String blogSearch(@PathVariable String tag, Model model, Principal user){
        if (user != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User usercheck = userRepo.findByUsername(userName);
            Iterable<Long> favourites = favRepository.findFavourites(usercheck.getId());
            List<Long> resultFavourites = new ArrayList<>();
            favourites.forEach(resultFavourites::add);
            model.addAttribute("favourites", favourites);
        }


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


    @PostMapping("/blog/search")
    public String blogSearchPost(@RequestParam String tag, Model model, Principal user){

        if (user != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User usercheck = userRepo.findByUsername(userName);
            Iterable<Long> favourites = favRepository.findFavourites(usercheck.getId());
            List<Long> resultFavourites = new ArrayList<>();
            favourites.forEach(resultFavourites::add);
            model.addAttribute("favourites", favourites);
        }


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

        try {
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
    public String sortPosts(@RequestParam(required = false) String sortBy ,@RequestParam(required = false) Long id, Model model, Principal user) {

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
