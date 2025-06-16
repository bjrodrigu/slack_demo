package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import com.example.demo.entities.Activity;


@RestController
public class DemoController {

   /**  @PostMapping("/slack")
        public String handleSlack() {
        return index(); // Reuse your GET method
        }

        */

        @GetMapping("/test")
        public Activity index() {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://bored-api.appbrewery.com/random";
        
            Activity activity = restTemplate.getForObject(url, Activity.class);
        
            return activity;
        }
}
