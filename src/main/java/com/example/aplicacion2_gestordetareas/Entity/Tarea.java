package com.example.aplicacion2_gestordetareas.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "tareas")
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTarea", nullable = false)
    private Integer id;

    @Column(name = "titulo", nullable = false, length = 45)
    private String titulo;

    @Column(name = "descripcion", nullable = false, length = 45)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "idPrioridad", nullable = false)
    private Prioridad prioridad;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @Column(name="fechaLimite" , nullable = false)
    private Date fechaLimite;

}