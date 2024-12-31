package com.example.excel.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Welcome to My Fancy Homepage");
        model.addAttribute("message", "Explore our features and enjoy your stay!");

        List<Map<String, String>> features = List.of(
                Map.of("icon", "fa-rocket", "title", "Fast Performance", "description", "Our application is lightning fast."),
                Map.of("icon", "fa-lock", "title", "Secure", "description", "We take security seriously."),
                Map.of("icon", "fa-code", "title", "Developer Friendly", "description", "Built with developers in mind.")
        );

        model.addAttribute("features", features);

        log.info("Home page accessed");
        return "home";
    }
}
