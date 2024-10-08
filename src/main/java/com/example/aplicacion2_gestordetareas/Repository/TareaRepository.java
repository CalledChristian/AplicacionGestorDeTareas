package com.example.aplicacion2_gestordetareas.Repository;

import com.example.aplicacion2_gestordetareas.Entity.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TareaRepository extends JpaRepository<Tarea,Integer> {

    //QueryMethods
   @Query(value="select * from tareas where idUsuario = ?1" , nativeQuery = true)
    List<Tarea> TareasPorUsuario(Integer idUsuario);

    @Query(value="select * from tareas where idUsuario = ?1 and (titulo like %?2% or descripcion like %?2%) " , nativeQuery = true)
    List<Tarea> buscarTareasPorUsuario(Integer idUsuario,String texto);

    /*
    @Query(value="select * from tareas where titulo like %?1% or descripcion like %?1%" , nativeQuery = true)
    List<Tarea> buscarTareas(String texto);
    */


}
