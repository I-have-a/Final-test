package com.niuma.questionnaire.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
    @GetMapping("/test")
    public String getIndex(String error,Model model) {
        model.addAttribute("msg", error);
        return "user/error";
    }

    @GetMapping("login")
    public String login() {
        return "user/login";
    }

    @RequestMapping("/")
    public String index(){
        return "index";
    }
}
