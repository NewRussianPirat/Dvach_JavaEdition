package com.example.dvach_javaedition.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminAuthentication {
    private static final String ADMIN_PASSWORD = "obamaobeziana";

    @PostMapping("/login")
    public String login(@RequestParam("password") String password, HttpServletRequest request, HttpSession session) {
        if (ADMIN_PASSWORD.equals(password)) {
            session.setAttribute("isAdmin", true);
        } else {
            request.setAttribute("errorMessage", "Неверный пароль");
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}
