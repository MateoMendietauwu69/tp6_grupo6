package ar.edu.unju.escmi.tp6.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ar.edu.unju.escmi.tp6.dominio.Detalle;
import ar.edu.unju.escmi.tp6.dominio.Factura;
import ar.edu.unju.escmi.tp6.dominio.TarjetaCredito;

class CreditoTest {

    @Test
    void testMontoCreditoValido() {
        Factura factura = new Factura();
        List<Detalle> detalles = new ArrayList<Detalle>();

        Detalle detalle1 = new Detalle();
        detalle1.setImporte(500000);

        Detalle detalle2 = new Detalle();
        detalle2.setImporte(900000);

        Detalle detalle3 = new Detalle();
        detalle3.setImporte(100000);

        detalles.add(detalle1);
        detalles.add(detalle2);
        detalles.add(detalle3);

        factura.setDetalles(detalles);

        double obtenido = factura.calcularTotal();
        double limitePermitido = 2500000;

        assertTrue(obtenido <= limitePermitido);
    }

    @Test
    void testSumaImportesDetallesIgualTotalFactura() {
        Factura factura = new Factura();
        List<Detalle> detalles = new ArrayList<Detalle>();

        Detalle detalle1 = new Detalle();
        detalle1.setImporte(500000);

        Detalle detalle2 = new Detalle();
        detalle2.setImporte(900000);

        detalles.add(detalle1);
        detalles.add(detalle2);

        factura.setDetalles(detalles);

        double expected = 1400000;
        double obtenido = factura.calcularTotal();

        assertEquals(expected, obtenido);
    }

    @Test
    void testMontoTotalNoSuperaLimiteCreditoYLimiteTarjeta() {
        Factura factura = new Factura();
        List<Detalle> detalles = new ArrayList<Detalle>();

        Detalle detalle1 = new Detalle();
        detalle1.setImporte(500000);

        detalles.add(detalle1);
        factura.setDetalles(detalles);

        TarjetaCredito tarjeta = new TarjetaCredito();
        tarjeta.setLimiteCompra(800000);

        double totalFactura = factura.calcularTotal();
        double limiteCreditoAhora20 = 2500000;

        assertTrue(totalFactura <= limiteCreditoAhora20);
        assertTrue(totalFactura <= tarjeta.getLimiteCompra());
    }
}
