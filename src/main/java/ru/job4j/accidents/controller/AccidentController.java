package ru.job4j.accidents.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.service.AccidentService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AccidentController {
    private final AccidentService accidentService;

    @GetMapping("/createAccident")
    public String viewCreateAccident(Model model) {
        model.addAttribute("user", "lostway");
        model.addAttribute("accident", new Accident());
        model.addAttribute("types", accidentService.getAllTypes());
        model.addAttribute("rules", accidentService.getAllRules());
        return "createAccident";
    }

    @PostMapping("/saveAccident")
    public String saveAccident(@ModelAttribute Accident accident, @RequestParam(value = "rIds", required = false) List<Integer> rIds) {
        accident.setRules(rIds.stream().map(i -> new Rule(i, null)).collect(Collectors.toSet()));
        accidentService.create(accident);
        return "redirect:/index";
    }

    @GetMapping("/editAccident")
    public String editAccident(@RequestParam("id") int id, Model model) {
        model.addAttribute("user", "lostway");
        Accident accident = accidentService.findById(id);
        model.addAttribute("accident", accident);
        model.addAttribute("types", accidentService.getAllTypes());
        model.addAttribute("rules", accidentService.getAllRules());
        return "editAccident";
    }

    @PostMapping("/confirmEdit")
    public String confirmEdit(@ModelAttribute Accident accident, @RequestParam(value = "rIds", required = false) List<Integer> rIds) {
        accident.setRules(rIds.stream().map(i -> new Rule(i, null)).collect(Collectors.toSet()));
        accidentService.update(accident);
        return "redirect:/index";
    }

    @GetMapping("/accidents")
    public String getAccidents(Model model) {
        model.addAttribute("user", "lostway");
        model.addAttribute("accidents", accidentService.getAllAccidents());
        return "accidents";
    }
}
