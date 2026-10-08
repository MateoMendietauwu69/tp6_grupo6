package ar.edu.unju.escmi.tp6.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import ar.edu.unju.escmi.tp6.dominio.Credito;

class CuotaTest {

    private Credito credito;

    @BeforeEach
    void setUp() {
        credito = new Credito(500000.0, LocalDate.now());
        credito.generarCuotas();
    }

    @Test
    void testListaCuotasNoEsNull() {
        assertNotNull(credito.getCuotas());
    }

    @Test
    void testCantidadCuotasGeneradas() {
        int expected = 20;
        int obtenido = credito.getCuotas().size();

        assertEquals(expected, obtenido);
    }

    @Test
    void testCantidadCuotasNoSuperaPermitido() {
        int obtenido = credito.getCuotas().size();
        int limiteMaximo = 20;

        assertTrue(obtenido <= limiteMaximo);
    }
}