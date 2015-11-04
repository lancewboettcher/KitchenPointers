package com.kitchenpointers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class JSPController {
	
    @RequestMapping("/test")
    public String test(ModelAndView modelAndView) {
    	System.out.println("heress");
        return "test";
    }
    
}
