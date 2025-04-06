package com.fondos.fondos_app.entity;

public class Cliente {
    private String clienteId;         // Mapea a "ClienteId"
    private String cedula;           // Mapea a "Cedula"
    private boolean fondo1;          // Mapea a "Fondo1"
    private boolean fondo2;          // Mapea a "Fondo2"
    private boolean fondo3;          // Mapea a "Fondo3"
    private boolean fondo4;          // Mapea a "Fondo4"
    private boolean fondo5;          // Mapea a "Fondo5"
    private int monto;               // Mapea a "Monto"
    private String nombre;           // Mapea a "Nombre"
    private String tipoNotificacion; // Mapea a "TipoNotificacion"

    public Cliente() {
    }

    // Constructor con parámetros principales
    public Cliente(String clienteId, String cedula, boolean fondo1, boolean fondo2,
                   boolean fondo3, boolean fondo4, boolean fondo5,
                   int monto, String nombre, String tipoNotificacion) {
        this.clienteId = clienteId;
        this.cedula = cedula;
        this.fondo1 = fondo1;
        this.fondo2 = fondo2;
        this.fondo3 = fondo3;
        this.fondo4 = fondo4;
        this.fondo5 = fondo5;
        this.monto = monto;
        this.nombre = nombre;
        this.tipoNotificacion = tipoNotificacion;
    }

    // Getters y setters
    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public boolean isFondo1() {
        return fondo1;
    }

    public void setFondo1(boolean fondo1) {
        this.fondo1 = fondo1;
    }

    public boolean isFondo2() {
        return fondo2;
    }

    public void setFondo2(boolean fondo2) {
        this.fondo2 = fondo2;
    }

    public boolean isFondo3() {
        return fondo3;
    }

    public void setFondo3(boolean fondo3) {
        this.fondo3 = fondo3;
    }

    public boolean isFondo4() {
        return fondo4;
    }

    public void setFondo4(boolean fondo4) {
        this.fondo4 = fondo4;
    }

    public boolean isFondo5() {
        return fondo5;
    }

    public void setFondo5(boolean fondo5) {
        this.fondo5 = fondo5;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(String tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }
}
