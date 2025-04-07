package com.fondos.fondos_app.controller;

import com.fondos.fondos_app.entity.Fondo;
import com.fondos.fondos_app.service.FondoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FondoController.class)
public class FondoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FondoService fondoService;

    @Test
    public void testGetAllFondosSuccess() throws Exception {
        Fondo fondo1 = new Fondo("fondo1", "Fondo A", 1000, "Categoria1");
        Fondo fondo2 = new Fondo("fondo2", "Fondo B", 2000, "Categoria2");
        List<Fondo> fondos = Arrays.asList(fondo1, fondo2);

        Mockito.when(fondoService.getAllFunds()).thenReturn(fondos);

        mockMvc.perform(get("/api/fund/funds"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fondoId").value("fondo1"))
                .andExpect(jsonPath("$[0].nombre").value("Fondo A"))
                .andExpect(jsonPath("$[0].montoMinimo").value(1000))
                .andExpect(jsonPath("$[0].categoria").value("Categoria1"))
                .andExpect(jsonPath("$[1].fondoId").value("fondo2"))
                .andExpect(jsonPath("$[1].nombre").value("Fondo B"))
                .andExpect(jsonPath("$[1].montoMinimo").value(2000))
                .andExpect(jsonPath("$[1].categoria").value("Categoria2"));
    }

    // Test GET /api/fund/funds when no funds are found.
    @Test
    public void testGetAllFondosNoContent() throws Exception {
        Mockito.when(fondoService.getAllFunds()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/fund/funds"))
                .andExpect(status().isNoContent());
    }

    // Test GET /api/fund/{id} for an existing fund.
    @Test
    public void testGetFondoByIdFound() throws Exception {
        Fondo fondo = new Fondo("fondo1", "Fondo A", 1000, "Categoria1");
        Mockito.when(fondoService.getFund("fondo1")).thenReturn(fondo);

        mockMvc.perform(get("/api/fund/{id}", "fondo1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fondoId").value("fondo1"))
                .andExpect(jsonPath("$.nombre").value("Fondo A"))
                .andExpect(jsonPath("$.montoMinimo").value(1000))
                .andExpect(jsonPath("$.categoria").value("Categoria1"));
    }

    // Test GET /api/fund/{id} when the fund does not exist.
    @Test
    public void testGetFondoByIdNotFound() throws Exception {
        Mockito.when(fondoService.getFund(anyString())).thenReturn(null);

        mockMvc.perform(get("/api/fund/{id}", "nonexistent"))
                .andExpect(status().isNotFound());
    }
}
