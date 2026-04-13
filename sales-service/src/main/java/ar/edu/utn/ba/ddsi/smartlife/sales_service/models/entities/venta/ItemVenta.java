package ar.edu.utn.ba.ddsi.smartlife.sales_service.models.entities.venta;

import ar.edu.utn.ba.ddsi.smartlife.sales_service.models.entities.productos.Producto;
import lombok.Getter;

@Getter
public class ItemVenta {

	private final Producto producto;
	private final int cantidad;

	public ItemVenta(Producto producto, int cantidad) {
		if (cantidad <= 0) {
			throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
		}
		this.producto = producto;
		this.cantidad = cantidad;
	}

	public double subtotalPrecioBase() {
		return producto.getPrecioBase() * cantidad;
	}

	public double totalImpuestos() {
		return producto.totalImpuestos() * cantidad;
	}

	public double totalFinal() {
		return producto.precioFinal() * cantidad;
	}

}
