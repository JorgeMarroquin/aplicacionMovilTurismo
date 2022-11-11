package com.example.turismo.models;

import java.util.Comparator;
import java.util.Date;

public class Lugar {
    private int id;
    private String nombre;
    private String descripcion;
    private TipoLugar tipoLugar;
    private String departamento;
    private Float latitud;
    private Float longitud;
    private String imagen;
    private Float calificacion;
    private Boolean isFavorite = false;
    private Date visitaFecha;
    private double distancia = 0;

    public Lugar() {}

    public Lugar(int id, String nombre, String descripcion, TipoLugar tipoLugar, String departamento, Float latitud, Float longitud, String imagen, Float calificacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoLugar = tipoLugar;
        this.departamento = departamento;
        this.latitud = latitud;
        this.longitud = longitud;
        this.imagen = imagen;
        this.calificacion = calificacion;
    }

    public Lugar(int id, String nombre, String descripcion, TipoLugar tipoLugar, String departamento, Float latitud, Float longitud, String imagen, Float calificacion, Boolean isFavorite, Date visitaFecha, double distancia) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoLugar = tipoLugar;
        this.departamento = departamento;
        this.latitud = latitud;
        this.longitud = longitud;
        this.imagen = imagen;
        this.calificacion = calificacion;
        this.isFavorite = isFavorite;
        this.visitaFecha = visitaFecha;
        this.distancia = distancia;
    }

    public int getId() {
        return this.id;
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

    public TipoLugar getTipoLugar() {
        return tipoLugar;
    }

    public void setTipoLugar(TipoLugar tipoLugar) {
        this.tipoLugar = tipoLugar;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public Float getLatitud() {
        return latitud;
    }

    public void setLatitud(Float latitud) {
        this.latitud = latitud;
    }

    public Float getLongitud() {
        return longitud;
    }

    public void setLongitud(Float longitud) {
        this.longitud = longitud;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Float calificacion) {
        this.calificacion = calificacion;
    }

    public Boolean getFavorite() { return isFavorite; }

    public void setFavorite(Boolean favorite) { isFavorite = favorite; }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getVisitaFecha() {
        return visitaFecha;
    }

    public void setVisitaFecha(Date visitaFecha) {
        this.visitaFecha = visitaFecha;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }
}
