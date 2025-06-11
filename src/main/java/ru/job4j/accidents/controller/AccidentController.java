package ru.job4j.accidents.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.AccidentService;

import java.util.Set;

@Controller
@RequiredArgsConstructor
public class AccidentController {
    private final AccidentService accidentService;

    @GetMapping("/createAccident")
    public String viewCreateAccident(Model model) {
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("accident", new Accident());
        model.addAttribute("types", accidentService.getAllTypes());
        model.addAttribute("rules", accidentService.getAllRules());
        return "createAccident";
    }

    @PostMapping("/saveAccident")
    public String saveAccident(@ModelAttribute Accident accident,
                               @RequestParam(value = "rIds", required = false)
                               Set<Integer> rIds, Model model) {
        try {
            accidentService.create(accident, rIds);
            return "redirect:/index";
        } catch (Exception e) {
            model.addAttribute("error", "Произошла ошибка при сохранении");
            return "errors/404";
        }
    }

    @GetMapping("/editAccident")
    public String editAccident(@RequestParam("id") int id, Model model) {
        try {
            model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            Accident accident = accidentService.findById(id);
            model.addAttribute("accident", accident);
            model.addAttribute("types", accidentService.getAllTypes());
            model.addAttribute("rules", accidentService.getAllRules());
            return "editAccident";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "errors/404";
        } catch (Exception e) {
            model.addAttribute("error", "Произошло исключение!");
            return "errors/404";
        }
    }

    @PostMapping("/confirmEdit")
    public String confirmEdit(@ModelAttribute Accident accident, @RequestParam(value = "rIds", required = false) Set<Integer> rIds, Model model) {
        try {
            accidentService.update(accident, rIds);
            return "redirect:/index";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "errors/404";
        } catch (Exception e) {
            model.addAttribute("error", "Произошла ошибка при редактировании инцидента");
            return "errors/404";
        }
    }

    @GetMapping("/accidents")
    public String getAccidents(Model model) {
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("accidents", accidentService.getAllAccidents());
        return "accidents";
    }
}
