package com.example.turismo.models;

public class Score {
    private int lugarId;
    private int usuarioId;
    private float score;

    public Score(int lugarId, int userId, float score) {
        this.lugarId = lugarId;
        this.usuarioId = userId;
        this.score = score;
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

    public void setUsuarioId(int userId) {
        this.usuarioId = userId;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
