package com.example.turismo.models;

import java.util.Date;

public class Comment {
    private int id;
    private int usuarioId;
    private Usuario usuario;
    private int lugarId;
    private Date fecha;
    private String comentario;
    private boolean isUser;

    public Comment() {
    }

    public Comment(int id, int usuarioId, Usuario usuario, int lugarId, Date fecha, String comentario) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.usuario = usuario;
        this.lugarId = lugarId;
        this.fecha = fecha;
        this.comentario = comentario;
    }

    public Comment(int id, int usuarioId, Usuario usuaro, int lugarId, Date fecha, String comentario, boolean isUser) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.usuario = usuaro;
        this.lugarId = lugarId;
        this.fecha = fecha;
        this.comentario = comentario;
        this.isUser = isUser;
    }

    public Comment(int usuarioId, int lugarId, Date fecha, String comentario) {
        this.usuarioId = usuarioId;
        this.lugarId = lugarId;
        this.fecha = fecha;
        this.comentario = comentario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getLugarId() {
        return lugarId;
    }

    public void setLugarId(int lugarId) {
        this.lugarId = lugarId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public boolean getIsUser() {
        return isUser;
    }

    public void setIsUser(boolean user) {
        isUser = user;
    }
}
