package com.example.aplicacion2_gestordetareas.Controller;


import com.example.aplicacion2_gestordetareas.Entity.Usuario;
import com.example.aplicacion2_gestordetareas.Repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping(value="/registro",method = RequestMethod.GET)
public class RegistroController {

    final UsuarioRepository usuarioRepository;

    public RegistroController(UsuarioRepository usuarioRepository) {

        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("")
    public String registro(){

        return "registro/registroForm";
    }

    @Transactional
    @PostMapping("/crearCuenta")
    public String crearCuenta(@RequestParam("nombre") String nombre, @RequestParam("apellido") String apellido,
                              @RequestParam("correo") String correo, @RequestParam("contrasenia") String contrasenia,
                              RedirectAttributes redirectAttributes){


        Optional<Usuario> usuarioOpt = usuarioRepository.buscarUsuarioPorCorreo(correo);

        if(usuarioOpt.isPresent()){
            redirectAttributes.addFlashAttribute("msg","El correo ya se encuentra registrado");
            return "redirect:/registro";
        }else{
           String contraseniaBycript = new BCryptPasswordEncoder().encode(contrasenia);
           usuarioRepository.crearUsuario(nombre,apellido,correo,contraseniaBycript);
           redirectAttributes.addFlashAttribute("msg","Cuenta Creada! \n Ya puede iniciar sesión");
           return "redirect:/login";
        }
    }
}
