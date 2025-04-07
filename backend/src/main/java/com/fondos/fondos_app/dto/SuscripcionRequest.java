package com.fondos.fondos_app.dto;

public class SuscripcionRequest {
    private String fondoId;
    private String email;

    public String getFondoId() {
        return fondoId;
    }
    public void setFondoId(String fondoId) {
        this.fondoId = fondoId;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
