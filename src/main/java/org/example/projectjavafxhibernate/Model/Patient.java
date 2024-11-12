package org.example.projectjavafxhibernate.Model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "correo_electronico")
    private String correoElectronico;

    @Column(name = "sexo")
    private String sexo;

    @Column(name = "grupo_sanguineo")
    private String grupoSanguineo;

    @Column(name = "estado_civil")
    private String estadoCivil;

    public Patient() {
    }

    public Patient(String nombre, String apellido, Date fechaNacimiento, String direccion, String telefono, String correoElectronico, String sexo, String grupoSanguineo, String estadoCivil) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.sexo = sexo;
        this.grupoSanguineo = grupoSanguineo;
        this.estadoCivil = estadoCivil;
    }
}
