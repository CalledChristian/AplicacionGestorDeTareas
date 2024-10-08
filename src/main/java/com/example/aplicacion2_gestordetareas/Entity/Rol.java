package com.example.aplicacion2_gestordetareas.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "rol")
public class Rol implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRol", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

}