package com.kitchenpointers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello() {
    	System.out.println("here");
        return "hello.html";
    }

}

