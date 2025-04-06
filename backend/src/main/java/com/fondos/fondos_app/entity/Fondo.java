package com.fondos.fondos_app.entity;

public class Fondo {
    private String FondoId;
    private String Nombre;
    private int MontoMinimo;
    private String Categoria;

    public Fondo() {}

    public Fondo(String FondoId, String nombre, int montoMinimo, String categoria) {
        this.FondoId = FondoId;
        this.Nombre = nombre;
        this.MontoMinimo = montoMinimo;
        this.Categoria = categoria;
    }

    public String getFondoId() {
        return FondoId;
    }
    public void setFondoId(String FondoId) {
        this.FondoId = FondoId;
    }
    public String getNombre() {
        return Nombre;
    }
    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }
    public int getMontoMinimo() {
        return MontoMinimo;
    }
    public void setMontoMinimo(int montoMinimo) {
        this.MontoMinimo = montoMinimo;
    }
    public String getCategoria() {
        return Categoria;
    }
    public void setCategoria(String categoria) {
        this.Categoria = categoria;
    }
}
