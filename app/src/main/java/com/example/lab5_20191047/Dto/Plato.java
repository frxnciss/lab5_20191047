package com.example.lab5_20191047.Dto;

public class Plato {
    private String nombre;
    private String kcal;

    public Plato(String nombre, String kcal) {
        this.nombre = nombre;
        this.kcal = kcal;
    }

    public String getNombre() {
        return nombre;
    }

    public String getKcal() {
        return kcal;
    }
}
