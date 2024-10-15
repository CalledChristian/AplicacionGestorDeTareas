package com.example.aplicacion2_gestordetareas.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
//Todos objeto que se desee guardar en sesión con java Spring,
//debe ser serializable, es decir, implementar la interfaz Serializable (java.io.Serializable)
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 45)
    private String apellido;

    @Column(name = "correo", nullable = false, length = 70)
    private String correo;

    @Column(name = "contrasenia", nullable = false, length = 300)
    private String contrasena;

    @ManyToOne
    @JoinColumn(name = "idRol", nullable = false)
    private Rol idRol;

    @Column(name = "estado", nullable = false)
    private int estado;

}