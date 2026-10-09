package ar.edu.unju.escmi.tp6.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ar.edu.unju.escmi.tp6.collections.CollectionCliente;
import ar.edu.unju.escmi.tp6.collections.CollectionCredito;
import ar.edu.unju.escmi.tp6.collections.CollectionFactura;
import ar.edu.unju.escmi.tp6.collections.CollectionProducto;
import ar.edu.unju.escmi.tp6.collections.CollectionStock;
import ar.edu.unju.escmi.tp6.collections.CollectionTarjetaCredito;
import ar.edu.unju.escmi.tp6.dominio.Cliente;
import ar.edu.unju.escmi.tp6.dominio.Credito;
import ar.edu.unju.escmi.tp6.dominio.Detalle;
import ar.edu.unju.escmi.tp6.dominio.Factura;
import ar.edu.unju.escmi.tp6.dominio.Producto;
import ar.edu.unju.escmi.tp6.dominio.Stock;
import ar.edu.unju.escmi.tp6.dominio.TarjetaCredito;

public class Main {

	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		
		CollectionTarjetaCredito.precargarTarjetas();
        CollectionCliente.precargarClientes();
        CollectionProducto.precargarProductos();
        CollectionStock.precargarStocks();
        
        int opcion = 0;
        do {
        	mostrarMenu();
        	try {
        		System.out.print("Ingrese su opción: ");
        		opcion = Integer.parseInt(scanner.nextLine().trim());
        		
        		switch (opcion) {
        			case 1:
        				realizarVentaAhora20();
        				break;
        			case 2:
        				verComprasCliente();
        				break;
        			case 3:
        				mostrarListaElectrodomesticosAhora20();
        				break;
        			case 4:
        				consultarStockElectrodomesticos();
        				break;
        			case 5:
        				revisarCreditosCliente();
        				break;
        			case 6:
        				System.out.println("\n¡Gracias por utilizar el sistema! Saliendo...");
        				break;
        			default:
        				System.out.println("\n>>> Opción no válida. Por favor, ingrese un número del 1 al 6.");
        		}
        	} catch (NumberFormatException e) {
        		System.out.println("\n>>> Error de ingreso: Debe ingresar un número entero válido.");
        	} catch (Exception e) {
        		System.out.println("\n>>> Ocurrió un error inesperado: " + e.getMessage());
        	} finally {
        		// Bloque finally para garantizar consistencia del estado del flujo
        	}
        } while (opcion != 6);
        
        scanner.close();
	}

	private static void mostrarMenu() {
		System.out.println("\n=======================================================");
		System.out.println("     SISTEMA DE VENTAS - PROGRAMA 'AHORA 20'");
		System.out.println("=======================================================");
		System.out.println("1- Realizar una venta con programa 'Ahora 20'.");
		System.out.println("2- Ver compras realizadas por el cliente (ingresando DNI).");
		System.out.println("3- Lista de los electrodomésticos que se pueden comprar con 'Ahora 20'.");
		System.out.println("4- Consultar stock de los electrodomésticos del programa.");
		System.out.println("5- Revisar los créditos de un cliente (ingresando DNI).");
		System.out.println("6- Salir.");
		System.out.println("=======================================================");
	}


	private static void realizarVentaAhora20() {
		System.out.println("\n--- Realizar Venta (Programa Ahora 20) ---");

		long dni = pedirLong("Ingrese el DNI del cliente: ");
		Cliente cliente = CollectionCliente.buscarCliente(dni);

		if (cliente == null) {
			System.out.println("\n>>> Cliente no encontrado con DNI: " + dni);
			String resp = pedirString("¿Desea registrar al nuevo cliente? (S/N): ");
			if (resp.equalsIgnoreCase("S")) {
				String nombre = pedirString("Ingrese el nombre completo: ");
				String direccion = pedirString("Ingrese la dirección: ");
				String telefono = pedirString("Ingrese el teléfono: ");
				cliente = new Cliente(dni, nombre, direccion, telefono);
				CollectionCliente.agregarCliente(cliente);
				System.out.println("✓ Cliente registrado exitosamente.");
			} else {
				System.out.println("Venta cancelada. Es necesario un cliente registrado.");
				return;
			}
		} else {
			System.out.println("Cliente seleccionado: " + cliente.getNombre());
		}

		long nroTarjeta = pedirLong("Ingrese el número de Tarjeta de Crédito del cliente: ");
		TarjetaCredito tarjeta = CollectionTarjetaCredito.buscarTarjetaCredito(nroTarjeta);

		if (tarjeta == null) {
			System.out.println("\n>>> Tarjeta de Crédito no encontrada.");
			String resp = pedirString("¿Desea registrar esta tarjeta para el cliente? (S/N): ");
			if (resp.equalsIgnoreCase("S")) {
				double limite = pedirDouble("Ingrese el límite de compra de la tarjeta: ");
				tarjeta = new TarjetaCredito(nroTarjeta, LocalDate.now().plusYears(3), cliente, limite);
				CollectionTarjetaCredito.agregarTarjetaCredito(tarjeta);
				System.out.println("✓ Tarjeta registrada exitosamente.");
			} else {
				System.out.println("Venta cancelada. Se requiere una tarjeta de crédito válida.");
				return;
			}
		} else {
			if (tarjeta.getCliente() == null || tarjeta.getCliente().getDni() != cliente.getDni()) {
				System.out.println("\n>>> Error: La tarjeta ingresada no pertenece al cliente seleccionado (DNI: " + dni + ").");
				return;
			}
		}

		List<Detalle> detalles = new ArrayList<>();
		double totalFactura = 0;
		double totalCelulares = 0;

		boolean continuarAgregando = true;
		while (continuarAgregando) {
			System.out.println("\n--- Selección de Producto ---");
			long codigoProducto = pedirLong("Ingrese el código del producto a comprar: ");
			Producto producto = CollectionProducto.buscarProducto(codigoProducto);

			if (producto == null) {
				System.out.println(">>> Error: No existe un producto con el código " + codigoProducto);
			} else if (!"Argentina".equalsIgnoreCase(producto.getOrigenFabricacion())) {
				System.out.println(">>> Error: El producto '" + producto.getDescripcion()
						+ "' no es de fabricación nacional (Origen: " + producto.getOrigenFabricacion()
						+ "). No está permitido en el programa Ahora 20.");
			} else {
				System.out.println("Producto encontrado: " + producto.getDescripcion() + " - $" + producto.getPrecioUnitario());
				int cantidad = pedirInt("Ingrese la cantidad deseada: ");

				if (cantidad <= 0) {
					System.out.println(">>> La cantidad debe ser mayor a cero.");
				} else {
					Stock stock = CollectionStock.buscarStock(producto);
					if (stock == null || stock.getCantidad() < cantidad) {
						int dispo = stock != null ? stock.getCantidad() : 0;
						System.out.println(">>> Stock insuficiente. Stock disponible actual: " + dispo);
					} else {
						double subtotal = cantidad * producto.getPrecioUnitario();
						boolean esCelular = producto.getDescripcion().toLowerCase().contains("celular")
								|| producto.getDescripcion().toLowerCase().contains("teléfono");

						if (esCelular && (totalCelulares + subtotal > 1000000.0)) {
							System.out.println(">>> Error: El límite para compra de teléfonos celulares en Ahora 20 es de $1.000.000.");
                            System.out.println("    Total celulares actual: $" + totalCelulares + " | Intentando agregar: $" + subtotal);
						} else if (totalFactura + subtotal > 2500000.0) {
							System.out.println(">>> Error: El monto total del crédito para Ahora 20 no puede superar los $2.500.000.");
                            System.out.println("    Total acumulado actual: $" + totalFactura + " | Intentando agregar: $" + subtotal);
						} else if (totalFactura + subtotal > tarjeta.getLimiteCompra()) {
							System.out.println(">>> Error: El monto total ($" + (totalFactura + subtotal)
									+ ") supera el límite disponible de la tarjeta ($" + tarjeta.getLimiteCompra() + ").");
						} else {
							Detalle detalle = new Detalle(cantidad, subtotal, producto);
							detalles.add(detalle);
							totalFactura += subtotal;
							if (esCelular) {
								totalCelulares += subtotal;
							}
							System.out.println("✓ Producto agregado con éxito. Subtotal acumulado: $" + totalFactura);
						}
					}
				}
			}

			if (detalles.isEmpty()) {
				String resp = pedirString("¿Desea intentar agregar otro producto? (S/N): ");
				if (!resp.equalsIgnoreCase("S")) {
					continuarAgregando = false;
				}
			} else {
				String resp = pedirString("¿Desea agregar otro producto a la compra? (S/N): ");
				if (!resp.equalsIgnoreCase("S")) {
					continuarAgregando = false;
				}
			}
		}

		if (detalles.isEmpty()) {
			System.out.println("\n>>> Venta cancelada. No se agregaron productos.");
			return;
		}

		long nroFactura = CollectionFactura.facturas.size() + 1;
		Factura factura = new Factura(LocalDate.now(), nroFactura, cliente, detalles);
		CollectionFactura.agregarFactura(factura);

		for (Detalle d : detalles) {
			Stock st = CollectionStock.buscarStock(d.getProducto());
			if (st != null) {
				CollectionStock.reducirStock(st, d.getCantidad());
			}
		}

		Credito credito = new Credito(tarjeta, factura, null);
		credito.generarCuotas();
		CollectionCredito.agregarCredito(credito);

		tarjeta.setLimiteCompra(tarjeta.getLimiteCompra() - factura.calcularTotal());

		System.out.println("\n=======================================================");
		System.out.println("       ¡VENTA REALIZADA CON ÉXITO (AHORA 20)!");
		System.out.println("=======================================================");
		System.out.println(factura);
		System.out.println("Detalle del Crédito generado (20 Cuotas Fijas):");
		credito.mostarCredito();
		System.out.println("Límite restante disponible en Tarjeta N° " + tarjeta.getNumero() + ": $" + tarjeta.getLimiteCompra());
	}

	private static void verComprasCliente() {
		System.out.println("\n--- Consultar Compras de Cliente ---");
		long dni = pedirLong("Ingrese el DNI del cliente: ");

		Cliente cliente = CollectionCliente.buscarCliente(dni);
		if (cliente == null) {
			System.out.println(">>> Cliente con DNI " + dni + " no encontrado.");
			return;
		}

		List<Factura> compras = cliente.consultarCompras();
		if (compras == null || compras.isEmpty()) {
			System.out.println("El cliente " + cliente.getNombre() + " (DNI: " + dni + ") no posee compras registradas.");
		} else {
			System.out.println("\nCompras realizadas por el cliente: " + cliente.getNombre() + " (DNI: " + dni + ")");
			for (Factura fac : compras) {
				System.out.println(fac);
			}
		}
	}

	private static void mostrarListaElectrodomesticosAhora20() {
		System.out.println("\n==========================================================================");
		System.out.println("   LISTA DE ELECTRODOMÉSTICOS INCLUIDOS EN EL PROGRAMA 'AHORA 20'");
		System.out.println("   (Requisito: Fabricación Nacional - Argentina)");
		System.out.println("==========================================================================");

		boolean hayProductos = false;
		for (Producto pro : CollectionProducto.productos) {
			if ("Argentina".equalsIgnoreCase(pro.getOrigenFabricacion())) {
				System.out.println("Código: " + pro.getCodigo()
						+ " | Desc: " + pro.getDescripcion()
						+ " | Precio: $" + pro.getPrecioUnitario()
						+ " | Origen: " + pro.getOrigenFabricacion());
				hayProductos = true;
			}
		}

		if (!hayProductos) {
			System.out.println("No hay productos nacionales disponibles en el sistema.");
		}
	}

	private static void consultarStockElectrodomesticos() {
		System.out.println("\n==========================================================================");
		System.out.println("        STOCK DE ELECTRODOMÉSTICOS INCLUIDOS EN EL PROGRAMA");
		System.out.println("==========================================================================");

		if (CollectionStock.stocks.isEmpty()) {
			System.out.println("No se registra stock de productos.");
		} else {
			for (Stock st : CollectionStock.stocks) {
				Producto p = st.getProducto();
				boolean esNacional = "Argentina".equalsIgnoreCase(p.getOrigenFabricacion());
				System.out.println("Código: " + p.getCodigo()
						+ " | Producto: " + p.getDescripcion()
						+ " | Stock Actual: " + st.getCantidad()
						+ " | Programa Ahora 20: " + (esNacional ? "SÍ (Nacional)" : "NO (Importado)"));
			}
		}
	}

	private static void revisarCreditosCliente() {
		System.out.println("\n--- Revisar Créditos de Cliente ---");
		long dni = pedirLong("Ingrese el DNI del cliente: ");

		Cliente cliente = CollectionCliente.buscarCliente(dni);
		if (cliente == null) {
			System.out.println(">>> Cliente con DNI " + dni + " no encontrado.");
			return;
		}

		boolean encontrado = false;
		System.out.println("\nCréditos registrados para el cliente: " + cliente.getNombre() + " (DNI: " + dni + ")");

		for (Credito cred : CollectionCredito.creditos) {
			if (cred.getTarjetaCredito() != null && cred.getTarjetaCredito().getCliente() != null
					&& cred.getTarjetaCredito().getCliente().getDni() == dni) {
				encontrado = true;
				System.out.println("\n-------------------------------------------------------");
				cred.mostarCredito();
			} else if (cred.getFactura() != null && cred.getFactura().getCliente() != null
					&& cred.getFactura().getCliente().getDni() == dni) {
				encontrado = true;
				System.out.println("\n-------------------------------------------------------");
				cred.mostarCredito();
			}
		}

		if (!encontrado) {
			System.out.println("El cliente " + cliente.getNombre() + " no posee créditos 'Ahora 20' registrados.");
		}
	}

	// =========================================================================
	// Métodos Auxiliares de Lectura con Manejo de Excepciones (Try-Catch-Finally)
	// =========================================================================

	private static long pedirLong(String mensaje) {
		long valor = 0;
		boolean valido = false;
		while (!valido) {
			try {
				System.out.print(mensaje);
				String entrada = scanner.nextLine().trim();
				valor = Long.parseLong(entrada);
				valido = true;
			} catch (NumberFormatException e) {
				System.out.println(">>> Error: Ingrese un número entero válido (sin letras ni caracteres especiales).");
			} finally {
				// Bloque de limpieza
			}
		}
		return valor;
	}

	private static int pedirInt(String mensaje) {
		int valor = 0;
		boolean valido = false;
		while (!valido) {
			try {
				System.out.print(mensaje);
				String entrada = scanner.nextLine().trim();
				valor = Integer.parseInt(entrada);
				valido = true;
			} catch (NumberFormatException e) {
				System.out.println(">>> Error: Ingrese un número entero válido.");
			} finally {
				// Bloque de limpieza
			}
		}
		return valor;
	}

	private static double pedirDouble(String mensaje) {
		double valor = 0.0;
		boolean valido = false;
		while (!valido) {
			try {
				System.out.print(mensaje);
				String entrada = scanner.nextLine().trim();
				valor = Double.parseDouble(entrada);
				valido = true;
			} catch (NumberFormatException e) {
				System.out.println(">>> Error: Ingrese un número decimal válido.");
			} finally {
				// Bloque de limpieza
			}
		}
		return valor;
	}

	private static String pedirString(String mensaje) {
		String texto = "";
		try {
			System.out.print(mensaje);
			texto = scanner.nextLine().trim();
		} catch (Exception e) {
			System.out.println(">>> Error en la lectura del texto: " + e.getMessage());
		} finally {
			// Bloque de limpieza
		}
		return texto;
	}
}

