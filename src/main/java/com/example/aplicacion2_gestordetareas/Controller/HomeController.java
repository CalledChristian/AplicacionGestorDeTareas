package com.example.aplicacion2_gestordetareas.Controller;


import com.example.aplicacion2_gestordetareas.Repository.TareaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/",method = RequestMethod.GET)
public class HomeController {

    final TareaRepository tareaRepository;

    public HomeController(TareaRepository tareaRepository) {

        this.tareaRepository = tareaRepository;
    }

    @GetMapping("")
    public String home(Model model){
        model.addAttribute("listaTareas", tareaRepository.findAll());
        return "index";
    }
}
