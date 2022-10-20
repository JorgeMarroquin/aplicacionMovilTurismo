package com.example.turismo.models;

import java.util.Date;

public class Visita {
    private int lugarId;
    private int usuarioId;
    private Date visitaFecha;

    public Visita(int lugarId, int usuarioId, Date visitaFecha) {
        this.lugarId = lugarId;
        this.usuarioId = usuarioId;
        this.visitaFecha = visitaFecha;
    }

    public int getLugarId() {
        return lugarId;
    }

    public void setLugarId(int lugarId) {
        this.lugarId = lugarId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Date getVisitaFecha() {
        return visitaFecha;
    }

    public void setVisitaFecha(Date visitaFecha) {
        this.visitaFecha = visitaFecha;
    }
}
