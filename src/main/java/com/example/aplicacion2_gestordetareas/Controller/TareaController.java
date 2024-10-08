package com.example.aplicacion2_gestordetareas.Controller;


import com.example.aplicacion2_gestordetareas.Entity.Prioridad;
import com.example.aplicacion2_gestordetareas.Entity.Tarea;
import com.example.aplicacion2_gestordetareas.Entity.Usuario;
import com.example.aplicacion2_gestordetareas.Repository.PrioridadRepository;
import com.example.aplicacion2_gestordetareas.Repository.TareaRepository;
import com.example.aplicacion2_gestordetareas.Repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping(value={"/tareas"},method = RequestMethod.GET)
public class TareaController {

    //repositorios:

    //forma 1 de invocacion
    final TareaRepository tareaRepository;

    final UsuarioRepository usuarioRepository;

    final PrioridadRepository prioridadRepository;

    private HttpSession session;

    public TareaController(TareaRepository tareaRepository, UsuarioRepository usuarioRepository, PrioridadRepository prioridadRepository, HttpSession session){
        this.tareaRepository = tareaRepository;
        this.usuarioRepository = usuarioRepository;
        this.prioridadRepository = prioridadRepository;
        this.session = session;
    }

    @GetMapping("")
    public String inicioTareas(Model model) {
        //obtenemos el usuario con session
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuario", usuarioLogueado);
        model.addAttribute("listaTareas", tareaRepository.TareasPorUsuario(usuarioLogueado.getId()));
        return "tareas/inicioTareas";
    }

    @GetMapping("/nuevo")
    public String nuevaTarea(Model model) {
        model.addAttribute("listaPrioridades",prioridadRepository.findAll());
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuario", usuarioLogueado);
        return "tareas/nuevaTarea";
    }

    @Transactional
    @PostMapping("/guardar")
    //Aplicando Mapeo de Objeto en Formulario: Data Binding
    public String guardarTarea(Tarea tarea,RedirectAttributes redirectAttributes ) {
        //dado que mapeamos todos los atributos del objeto = name en el formulario
        //Data binding

        //ahora verificamos si la tarea es nueva o ya existe

        if(tarea.getId()!=null) {
            //si la tarea ya existe,entonces mandamos el siguiente mensaje
            redirectAttributes.addFlashAttribute("msg","Tarea Editada Exitosamente");
        }else{
            //sino:
            redirectAttributes.addFlashAttribute("msg","Tarea Guardada Exitosamente");
        }
        //entonces aplicamos la funcion save para guardar o actualizar la tarea
        tareaRepository.save(tarea);
        //usamos redirect atributes para añadir atributos flash , como el msg de nueva tarea creada
        return "redirect:/tareas";
    }


    @Transactional
    @GetMapping("/editar")
    //Aplicando Mapeo de Objeto en Formulario: Data Binding
    public String editarTarea(@RequestParam("id") int idTarea , Model model) {
        Optional<Tarea> tarea = tareaRepository.findById(idTarea);
        if(tarea.isPresent()) {
            model.addAttribute("tarea",tarea.get());
        }
        model.addAttribute("listaPrioridades",prioridadRepository.findAll());
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuario", usuarioLogueado);
        return "tareas/editarTarea";
    }

    //Ojo que la ruta de eliminar es "GET"
    @GetMapping("/borrar")
    public String borrarTarea(@RequestParam("id") int idTarea ,
                              RedirectAttributes redirectAttributes) {

        Optional<Tarea> optTarea = tareaRepository.findById(idTarea);

        if(optTarea.isPresent()){
            tareaRepository.delete(optTarea.get());
        }
        //usamos el redirect
        redirectAttributes.addFlashAttribute("msg2","Tarea Borrada");
        //ojo que redirect: no es solo para "postmapping" ,
        //Y recordar también que el uso de redirectAtributtes
        //es exclusivo cuando vamos a redirigir a otra pagina
        return "redirect:/tareas";
    }

    @Transactional
    @PostMapping("/buscar")
    public String buscarTarea(@RequestParam("idUser") int idUsuario,@RequestParam("texto") String texto, RedirectAttributes redirectAttributes, Model model) {

        //Si el texto ingresado del buscador No es vacio (Not Empty)
        //if(!texto.isEmpty()){
            //luego validamos si hay coincidencias con el texto ingresado
            if(!tareaRepository.buscarTareasPorUsuario(idUsuario,texto).isEmpty()) {
                //si la lista no es vacia, entonces ponemos model atributte y retornamos la vista "tareas/inicioTareas"
                model.addAttribute("listaTareas",tareaRepository.buscarTareasPorUsuario(idUsuario,texto));
                return "tareas/inicioTareas";
            }else{
                redirectAttributes.addFlashAttribute("msg2","No hay Tareas Coincidentes");
                return "redirect:/tareas";
            }
        /*Version.Original con redirect y flash attributes
            (se reemplazó con JQuery)
            else{
            redirectAttributes.addFlashAttribute("msg2","Debe ingresar texto");
            return "redirect:/tareas";
        }*/
    }


}
