package ar.edu.utn.ba.ddsi.smartlife.sales_service.models.entities.productos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

	private Long id;
	private TipoProducto tipo;
	private double precioBase;
	private String descripcion;

	public double totalImpuestos() {
		return this.tipo.totalImpuestos(this);
	}

	public double precioFinal() {
		return this.precioBase + this.totalImpuestos();
	}

}
