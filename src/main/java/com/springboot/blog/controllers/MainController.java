package com.springboot.blog.controllers;


import java.io.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import com.itextpdf.html2pdf.HtmlConverter;

import com.springboot.blog.models.About;
import com.springboot.blog.models.Home;
import com.springboot.blog.models.Post;
import com.springboot.blog.repo.AboutRepo;
import com.springboot.blog.repo.HomeRepo;
import com.springboot.blog.repo.UserRepo;
import com.springboot.blog.weather.Weather;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.tools.JavaFileObject.Kind.HTML;


@Controller
public class MainController {

    @Autowired
    private AboutRepo aboutRepo;

    @Autowired
    private HomeRepo homeRepo;

    @GetMapping("/")
    public String home(Model model) {
        Iterable <Home> home = homeRepo.findAll();
        List<Home> homePost = new ArrayList<>();
        home.forEach(homePost::add);
        if(homePost.isEmpty()){
            homePost.add(new Home(1L,"empty","empty"));
        }
        model.addAttribute("homePost", homePost);
        return "home";
    }

    @PostMapping("/")
    public String homeadd(@RequestParam String home, @RequestParam String photourl, Model model) {
            Home homeObject = new Home(1L, home,photourl);
            homeRepo.save(homeObject);

        return "redirect:/";
    }

    @PostMapping("/about")
    public String aboutAdd(@RequestParam String about, @RequestParam String photourl, Model model) {
        About aboutObject = new About(1L, about,photourl);
        aboutRepo.save(aboutObject);
        return "redirect:/about";
    }


    @PostMapping("/about/pdf")
    public ResponseEntity<InputStreamResource> aboutDownloader( HttpServletResponse response,HttpServletRequest request) throws FileNotFoundException {
            Iterable <About> about = aboutRepo.findAll();
            List<About> aboutPost = new ArrayList<>();
            about.forEach(aboutPost::add);
             HtmlConverter.convertToPdf(aboutPost.get(0).getAbout().replaceAll("(\r\n|\n)", "<br />"), new FileOutputStream("CV_Andrey_(generated).pdf"));

            File file = new File("CV_Andrey_(generated).pdf");
            HttpHeaders respHeaders = new HttpHeaders();
            MediaType mediaType = MediaType.parseMediaType("application/pdf");
            respHeaders.setContentType(mediaType);
            respHeaders.setContentLength(file.length());
            respHeaders.setContentDispositionFormData("attachment", file.getName());
            InputStreamResource isr = new InputStreamResource(new FileInputStream(file));
            return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
    }


    @GetMapping("/about")
    public String about(Model model) {
        Iterable <About> about = aboutRepo.findAll();
        List<About> aboutPost = new ArrayList<>();
        about.forEach(aboutPost::add);

        if(aboutPost.isEmpty()){
            aboutPost.add(new About(1L,"empty","empty"));
        }

        model.addAttribute("aboutPost", aboutPost);
        return "about";
    }

    @GetMapping("/parse")
    public String parser() {
        return "parse";
    }

    @PostMapping("/parse")
    public String getweather(@RequestParam String city, Model model){
        


if(city==""){
            city="Helsinki";
        }

try {

        String showcity = city;
        Weather a = new Weather();
        String jsonString = a.getUrlContent("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=fa11a742a4d2e48ad88945dcea73facb&units=metric");
        JSONObject obj = new JSONObject(jsonString);
        double tempd= obj.getJSONObject("main").getDouble("temp");
        double temp_mind = obj.getJSONObject("main").getDouble("temp_min");
        double temp_maxd = obj.getJSONObject("main").getDouble("temp_max");
        double feelsd = obj.getJSONObject("main").getDouble("feels_like");
        double humidity = obj.getJSONObject("main").getDouble("humidity");


//        double tempd  = Double.parseDouble(temp);
        int tempi= (int) Math. round(tempd);

//        double temp_mind  = Double.parseDouble(temp_min);
        int temp_mini= (int) Math. round(temp_mind);

//        double temp_maxd  = Double.parseDouble(temp_max);
        int temp_maxi= (int) Math. round(temp_maxd);

//        double feelsd = Double.parseDouble(feels);
        int feelsi= (int) Math. round(feelsd);

        String[] elements = jsonString.split("\"");


        model.addAttribute("temp", tempi);
        model.addAttribute("temp_min", temp_mini);
        model.addAttribute("temp_max", temp_maxi);
        model.addAttribute("feels", feelsi);
        model.addAttribute("humidity", humidity);
        model.addAttribute("cityshow", elements[elements.length-4]);

} catch (Exception e){
    System.out.print(city + " not found - 404");
}
        return "parse";


    }


}
