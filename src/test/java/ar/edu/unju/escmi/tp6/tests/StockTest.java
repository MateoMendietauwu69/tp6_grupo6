package ar.edu.unju.escmi.tp6.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ar.edu.unju.escmi.tp6.collections.CollectionStock;
import ar.edu.unju.escmi.tp6.dominio.Producto;
import ar.edu.unju.escmi.tp6.dominio.Stock;

class StockTest {

    @Test
    void testDecrementoStock() {
        Producto producto = new Producto(101, "Smart TV", 800000.0, "Argentina");
        Stock stock = new Stock(10, producto);
        CollectionStock.stocks.add(stock);

        CollectionStock.reducirStock(stock, 3);

        int expected = 7;
        int obtenido = stock.getCantidad();

        assertEquals(expected, obtenido);
    }
}

