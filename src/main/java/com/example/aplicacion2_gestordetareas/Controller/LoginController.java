package com.example.aplicacion2_gestordetareas.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/login",method = RequestMethod.GET)
public class LoginController {

    @GetMapping("")
    public String login(){
        return "login/loginForm";
    }
}
