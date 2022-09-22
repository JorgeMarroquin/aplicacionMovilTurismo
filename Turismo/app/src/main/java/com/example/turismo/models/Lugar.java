package com.example.turismo.models;

import java.util.Comparator;

public class Lugar {
    private int Id;
    private String nombre = "SIUUU";
    private TipoLugar tipoLugar;
    private String departamento = "guate";
    private Float latitud;
    private Float longitud;
    private String imagen;
    private Float calificacion;

    public Lugar() {}

    public Lugar(int id, String nombre, TipoLugar tipoLugar, String departamento, Float latitud, Float longitud, String imagen, Float calificacion) {
        Id = id;
        this.nombre = nombre;
        this.tipoLugar = tipoLugar;
        this.departamento = departamento;
        this.latitud = latitud;
        this.longitud = longitud;
        this.imagen = imagen;
        this.calificacion = calificacion;
    }

    public static Comparator<Lugar> nameAZComparator = new Comparator<Lugar>() {
        @Override
        public int compare(Lugar l1, Lugar l2) {
            return l1.getNombre().compareTo(l2.getNombre());
        }
    };

    public static Comparator<Lugar> nameZAComparator = new Comparator<Lugar>() {
        @Override
        public int compare(Lugar l1, Lugar l2) {
            return l2.getNombre().compareTo(l1.getNombre());
        }
    };

    public static Comparator<Lugar> departamentoAZComparator = new Comparator<Lugar>() {
        @Override
        public int compare(Lugar l1, Lugar l2) {
            return l1.getDepartamento().compareTo(l2.getDepartamento());
        }
    };

    public static Comparator<Lugar> departamentoZAComparator = new Comparator<Lugar>() {
        @Override
        public int compare(Lugar l1, Lugar l2) {
            return l2.getDepartamento().compareTo(l1.getDepartamento());
        }
    };

    public static Comparator<Lugar> menorPopularidad = new Comparator<Lugar>() {
        @Override
        public int compare(Lugar l1, Lugar l2) {
            return Float.compare(l1.getCalificacion(), l2.getCalificacion());
        }
    };

    public static Comparator<Lugar> mayorPopularidad = new Comparator<Lugar>() {
        @Override
        public int compare(Lugar l1, Lugar l2) {
            return Float.compare(l2.getCalificacion(), l1.getCalificacion());
        }
    };



    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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
}
