package com.example.excel.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        log.info("Client IP: {}", getClientIp());
        log.info("User Agent: {}", getUserAgent());
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

    public static String getClientIp() {
        HttpServletRequest request =
                (HttpServletRequest) RequestContextHolder
                        .getRequestAttributes()
                        .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        if (request == null) {
            return "Unknown";
        }

        // X-Forwarded-For 헤더에서 실제 IP 확인 (Proxy 환경 대비)
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getUserAgent() {
        HttpServletRequest request =
                (HttpServletRequest) RequestContextHolder
                        .getRequestAttributes()
                        .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        if (request == null) {
            return "Unknown";
        }

        return request.getHeader("User-Agent");
    }
}
