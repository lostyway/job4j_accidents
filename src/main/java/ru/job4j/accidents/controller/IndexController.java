package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.IAccidentService;

import java.util.List;

@Controller
@AllArgsConstructor
public class IndexController {
    private IAccidentService accidentService;

    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("user", "lostway");
        List<Accident> accidents = accidentService.getAllAccidents();
        model.addAttribute("accidents", accidents);
        return "index";
    }
}
