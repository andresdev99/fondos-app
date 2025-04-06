package com.fondos.fondos_app.entity;

public class Transaccion {
    private String ClienteId;
    private String TransaccionId;
    private String Tipo; // "apertura" o "cancelacion"
    private String FondoId;
    private String Fecha; // Formato ISO 8601

    public Transaccion() {}

    public Transaccion(String clienteId, String transaccionId, String tipo, String FondoId, String fecha) {
        this.ClienteId = clienteId;
        this.TransaccionId = transaccionId;
        this.Tipo = tipo;
        this.FondoId = FondoId;
        this.Fecha = fecha;
    }

    // Getters y Setters
    public String getClienteId() {
        return ClienteId;
    }
    public void setClienteId(String clienteId) {
        this.ClienteId = clienteId;
    }
    public String getTransaccionId() {
        return TransaccionId;
    }
    public void setTransaccionId(String transaccionId) {
        this.TransaccionId = transaccionId;
    }
    public String getTipo() {
        return Tipo;
    }
    public void setTipo(String tipo) {
        this.Tipo = tipo;
    }
    public String getFondoId() {
        return FondoId;
    }
    public void setFondoId(String FondoId) {
        this.FondoId = FondoId;
    }
    public String getFecha() {
        return Fecha;
    }
    public void setFecha(String fecha) {
        this.Fecha = fecha;
    }
}
