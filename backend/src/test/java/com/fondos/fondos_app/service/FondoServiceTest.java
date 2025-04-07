package com.fondos.fondos_app.service;

import com.fondos.fondos_app.entity.Fondo;
import com.fondos.fondos_app.repository.FondoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FondoServiceTest {

    @Mock
    private FondoRepository fondoRepository;

    @InjectMocks
    private FondoService fondoService;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    // Test getFund returns the correct fund when it exists
    @Test
    public void testGetFundWhenExists() {
        String fondoId = "fondo1";
        Fondo fondo = new Fondo(fondoId, "Fondo A", 1000, "Categoria1");
        when(fondoRepository.findById(fondoId)).thenReturn(fondo);

        Fondo result = fondoService.getFund(fondoId);
        assertNotNull(result, "Expected a non-null Fondo object");
        assertEquals(fondoId, result.getFondoId(), "FondoId should match");
    }

    // Test getFund returns null when the fund doesn't exist
    @Test
    public void testGetFundWhenDoesNotExist() {
        String fondoId = "fondoX";
        when(fondoRepository.findById(fondoId)).thenReturn(null);

        Fondo result = fondoService.getFund(fondoId);
        assertNull(result, "Expected null when fund does not exist");
    }

    // Test getFundName returns the correct name when the fund exists
    @Test
    public void testGetFundNameWhenExists() {
        String fondoId = "fondo1";
        Fondo fondo = new Fondo(fondoId, "Fondo A", 1000, "Categoria1");
        when(fondoRepository.findById(fondoId)).thenReturn(fondo);

        String fundName = fondoService.getFundName(fondoId);
        assertEquals("Fondo A", fundName, "Fund name should match the expected value");
    }

    // Test getFundName returns null when the fund doesn't exist
    @Test
    public void testGetFundNameWhenDoesNotExist() {
        String fondoId = "fondoX";
        when(fondoRepository.findById(fondoId)).thenReturn(null);

        String fundName = fondoService.getFundName(fondoId);
        assertNull(fundName, "Expected null when fund does not exist");
    }

    // Test getAllFunds returns the correct list of funds
    @Test
    public void testGetAllFunds() {
        Fondo fondo1 = new Fondo("fondo1", "Fondo A", 1000, "Categoria1");
        Fondo fondo2 = new Fondo("fondo2", "Fondo B", 2000, "Categoria2");
        List<Fondo> fondos = Arrays.asList(fondo1, fondo2);
        when(fondoRepository.findAll()).thenReturn(fondos);

        List<Fondo> result = fondoService.getAllFunds();
        assertEquals(2, result.size(), "Expected 2 funds in the list");
        assertEquals("Fondo A", result.get(0).getNombre(), "First fund's name should be 'Fondo A'");
        assertEquals("Fondo B", result.get(1).getNombre(), "Second fund's name should be 'Fondo B'");
    }
}
