package com.fondos.fondos_app.entity;

public class Cliente {
    private String clienteId;
    private String cedula;
    private boolean fondo1;
    private boolean fondo2;
    private boolean fondo3;
    private boolean fondo4;
    private boolean fondo5;
    private int monto;
    private String nombre;
    private String tipoNotificacion;
    private String email;

    public Cliente() {
    }

    public Cliente(String clienteId, String cedula, boolean fondo1, boolean fondo2,
                   boolean fondo3, boolean fondo4, boolean fondo5,
                   int monto, String nombre, String tipoNotificacion, String email) {
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
        this.email = email;
    }

    // Getters y setters
    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
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
