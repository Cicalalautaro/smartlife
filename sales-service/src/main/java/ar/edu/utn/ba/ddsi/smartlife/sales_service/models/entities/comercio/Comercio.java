package ar.edu.utn.ba.ddsi.smartlife.sales_service.models.entities.comercio;

import ar.edu.utn.ba.ddsi.smartlife.sales_service.models.entities.observers.ObservadorVenta;
import ar.edu.utn.ba.ddsi.smartlife.sales_service.models.entities.productos.Producto;
import ar.edu.utn.ba.ddsi.smartlife.sales_service.models.entities.venta.ItemVenta;
import ar.edu.utn.ba.ddsi.smartlife.sales_service.models.entities.venta.Venta;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Comercio {

	private long id;
	private List<Producto> productos;
    private List<Venta> ventas;
	private List<ObservadorVenta> observadores;

	public Comercio(long id) {
		this.id = id;
		this.productos = new ArrayList<>();
		this.observadores = new ArrayList<>();
        this.ventas = new ArrayList<>();
	}

    public void agregarProducto(Producto ... productos) {
        Collections.addAll(this.productos, productos);
    }

    public void agregarObservadores(ObservadorVenta ... observadores) {
        Collections.addAll(this.observadores, observadores);
    }

    public void agregarVenta(Venta venta) {
        if(!sonTodosProductosPropios(venta.getItems().stream().map(ItemVenta::getProducto).toList())) {
            throw new IllegalArgumentException("El comercio no puede registrar ventas de productos que no vende");
        }
        this.ventas.add(venta);
        this.observadores.forEach(o -> o.serNotificadoDe(venta));
    }

	public void eliminarObservador(ObservadorVenta observador) {
		this.observadores.remove(observador);
	}

    private boolean sonTodosProductosPropios(List<Producto> productos) {
        return this.productos.containsAll(productos);
    }
}
