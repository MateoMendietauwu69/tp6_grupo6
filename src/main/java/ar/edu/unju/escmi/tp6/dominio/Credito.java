package ar.edu.unju.escmi.tp6.dominio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Credito {

	private static final int ccoutas = 20;
	private static final double lcredito = 2500000.0;
	private TarjetaCredito tarjetaCredito;
	private Factura factura;
	private List<Cuota> cuotas = new ArrayList<>();

	public Credito() {
	}

	public Credito(TarjetaCredito tarjetaCredito, Factura factura, List<Cuota> cuotas) {
		this.tarjetaCredito = tarjetaCredito;
		this.factura = factura;
		this.cuotas = cuotas != null ? cuotas : new ArrayList<>();
		generarCuotas();
	}

	public Credito(List<Cuota> cuotas) {
		this.cuotas = cuotas != null ? cuotas : new ArrayList<>();
	}

	public Credito(double monto, LocalDate fecha) {
		this.factura = new Factura();
		Detalle detalle = new Detalle();
		detalle.setImporte(monto);

		List<Detalle> detalles = new ArrayList<>();
		detalles.add(detalle);
		this.factura.setDetalles(detalles);

		this.cuotas = new ArrayList<>();
		generarCuotas(fecha);
	}

	public TarjetaCredito getTarjetaCredito() {
		return tarjetaCredito;
	}

	public void setTarjetaCredito(TarjetaCredito tarjetaCredito) {
		this.tarjetaCredito = tarjetaCredito;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public List<Cuota> getCuotas() {
		return cuotas;
	}

	public void setCuotas(List<Cuota> cuotas) {
		this.cuotas = cuotas != null ? cuotas : new ArrayList<>();
	}
	
	public void generarCuotas() {
		generarCuotas(LocalDate.now());
	}

	private void generarCuotas(LocalDate fecha) {
		if(factura == null) throw new IllegalStateException("No se puede genear cuotas sin factura");

		double montototal = factura.calcularTotal();

		if(montototal <= 0 || montototal > lcredito) throw new IllegalStateException("El monto debe ser mayor a 0 y no superar los $2,500,000");

		cuotas.clear();

		double montocuotas = Math.round((montototal / ccoutas));
		double acumulado = 0;

		for(int i = 1; i<= ccoutas; i++) {
			double importe;
			if(i == ccoutas) importe = Math.round(montototal - acumulado);
			else {
				importe = montocuotas;
				acumulado += importe;
			}
			LocalDate vencimiento = fecha.plusMonths(i);
			Cuota cuota = new Cuota(importe, i, fecha, vencimiento);
			cuotas.add(cuota);
		}
	}

	public void mostarCredito() {
		System.out.println("Tarjeta De Credito: " + tarjetaCredito + "\n" + factura + "\nCant. Cuotas:\n");
		for(Cuota cuota: cuotas) {
			System.out.println(cuota);
		}
	}
}