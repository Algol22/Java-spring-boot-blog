package com.springboot.blog.restcontroller;


import com.springboot.blog.exceptions.NotFoundException;
import com.springboot.blog.models.Post;
import com.springboot.blog.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api")
public class ApiRestController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping(produces = "application/json;charset=UTF-8")
    public Iterable <Post> postLists() {
        Iterable <Post> allPostsFind = postRepository.findAll();
        List<Post> allPostsList = new ArrayList<>();
        allPostsFind.forEach(allPostsList::add);

        return allPostsFind;
    }
//    // GET all
//    fetch('https://jsprblog.herokuapp.com/api/').then(response => response.json().then(console.log))


    @GetMapping("{id}")
    public Post postById(@PathVariable Long id){
        Post post = postRepository.findById(id).orElseThrow(NotFoundException::new);
        return post;
    }
//    // GET one
//    fetch('/api/407').then(response => response.json().then(console.log))



    @PostMapping
    public Post addPost(@RequestBody Post postnew) {
        postRepository.save(postnew);
        return postnew;
    }

//     POST add new one
//    fetch(
//  'https://jsprblog.herokuapp.com/api',
//    {
//        method: 'POST',
//                headers: { 'Content-Type': 'application/json' },
//        body: JSON.stringify({ title: 'test', anons: 'test', full_text: 'test',  tag:'test', thedate: '2021-12-29' })
//    }
//).then(result => result.json().then(console.log))





    @PutMapping("{id}")
    public void update(@RequestBody Post posted, @PathVariable Long id) {
        Date thedate = new Date();
    postRepository.updateById(id,posted.getAnons(), posted.getFull_text(), posted.getTag(), thedate, posted.getTitle());
    }
//    // PUT update
//    fetch(
//  'https://jsprblog.herokuapp.com/api/278',
//    {
//        method: 'PUT',
//                headers: { 'Content-Type': 'application/json' },
//        body: JSON.stringify({ title: 'test PUT put put', anons: 'test', full_text: 'test',  tag:'test', thedate: '2021-12-29'})
//    }
//).then(result => result.json().then(console.log))





    @DeleteMapping("{id}")
    public void delete (@PathVariable Long id){
     postRepository.deleteById(id);
    }
//    // DELETE existing
//    fetch('https://jsprblog.herokuapp.com/api/279', { method: 'DELETE' }).then(result => console.log(result))


}
