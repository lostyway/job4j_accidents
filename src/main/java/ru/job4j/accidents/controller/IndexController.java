package ru.job4j.accidents.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping({"/index", "/"})
    public String getIndex(Model model) {
        model.addAttribute("user",  SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "index";
    }
}
