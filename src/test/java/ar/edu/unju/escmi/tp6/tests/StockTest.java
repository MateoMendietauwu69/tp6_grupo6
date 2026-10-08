package ar.edu.unju.escmi.tp6.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ar.edu.unju.escmi.tp6.dominio.Producto;

class StockTest {

    @Test
    void testDecrementoStock() {
        Producto producto = new Producto(101, "Smart TV", 800000.0, 10);

        producto.decrementarStock(3);

        int expected = 7;
        int obtenido = producto.getStock();

        assertEquals(expected, obtenido);
    }
}
