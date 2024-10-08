package com.example.aplicacion2_gestordetareas.Repository;

import com.example.aplicacion2_gestordetareas.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {

    //QueryMethod para obtener el usuario logueado según su correo (o username)
    Usuario findByCorreo(String correo);

    @Query(value="select * from usuarios where correo = ?",nativeQuery = true)
    Optional<Usuario> buscarUsuarioPorCorreo(String correo);

    @Modifying
    @Query(value="insert into usuarios (nombre,apellido,correo,contrasenia,idRol,estado) values (?,?,?,?,3,1)",nativeQuery=true)
    void crearUsuario(String nombre,String apellido,String correo, String contrasenia);
}
