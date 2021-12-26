package com.springboot.blog.controllers;

import com.springboot.blog.weather.Weather;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
public class MainController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Main");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About");
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
