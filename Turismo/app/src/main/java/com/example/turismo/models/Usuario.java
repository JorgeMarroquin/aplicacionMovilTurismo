package com.example.turismo.models;

import java.io.Serializable;

public class Usuario implements Serializable {
    private int id;
    private String nombre;
    private String apellido;
    private String correo;
    private String password;
    private String nacionalidad;
    private String telefono;

    public Usuario() {}

    public Usuario(int id, String nombre, String apellido, String correo, String password, String nacionalidad, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.password = password;
        this.nacionalidad = nacionalidad;
        this.telefono = telefono;
    }

    public Usuario(String nombre, String apellido, String correo, String password, String nacionalidad, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.password = password;
        this.nacionalidad = nacionalidad;
        this.telefono = telefono;
    }

    public Boolean emptyFields(){
        if(this.nombre.matches("") || this.apellido.matches("") || this.correo.matches("") || this.password.matches("") ||
        this.nacionalidad.matches("") || this.telefono.matches("")){
            return true;
        }else{
            return false;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
