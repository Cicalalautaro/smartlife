package ar.edu.utn.ba.ddsi.smartlife.sales_service.models.entities.impuestos;

import ar.edu.utn.ba.ddsi.smartlife.sales_service.models.entities.productos.Producto;
import lombok.Getter;
import lombok.Setter;

public class IVA implements Impuesto {

	@Getter
    @Setter
	private static double porcentaje = 0.21;

	@Override
	public double calcular(Producto producto) {
		return IVA.porcentaje * producto.getPrecioBase();
	}
}
