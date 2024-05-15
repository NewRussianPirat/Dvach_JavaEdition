package com.example.dvach_javaedition.controller;

import com.example.dvach_javaedition.model.Section;
import com.example.dvach_javaedition.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainPageController {
    @Autowired
    private SectionService sectionService;

    @GetMapping("/")
    public String getSectionsMain(Model model) {
        List<Section> sections = sectionService.findAll();
        model.addAttribute("mainSectionList", sections);
        return "main";
    }
}
