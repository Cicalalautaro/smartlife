package ar.edu.utn.ba.ddsi.smartlife.sales_service.domain;

import ar.edu.utn.ba.ddsi.smartlife.sales_service.models.entities.comercio.Comercio;
import ar.edu.utn.ba.ddsi.smartlife.sales_service.models.entities.observers.ObservadorSffa;
import ar.edu.utn.ba.ddsi.smartlife.sales_service.models.entities.observers.ObservadorSvibaa;
import ar.edu.utn.ba.ddsi.smartlife.sales_service.utils.GeneradorIdSecuencial;
import ar.edu.utn.ba.ddsi.smartlife.sales_service.models.entities.impuestos.EI;
import ar.edu.utn.ba.ddsi.smartlife.sales_service.models.entities.impuestos.EO;
import ar.edu.utn.ba.ddsi.smartlife.sales_service.models.entities.impuestos.IVA;
import ar.edu.utn.ba.ddsi.smartlife.sales_service.models.entities.productos.Producto;
import ar.edu.utn.ba.ddsi.smartlife.sales_service.models.entities.productos.TipoProducto;
import ar.edu.utn.ba.ddsi.smartlife.sales_service.models.entities.venta.ItemVenta;
import ar.edu.utn.ba.ddsi.smartlife.sales_service.models.entities.venta.Venta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DominioSalesServiceTest {

	private static final double PRECIO_BASE_EJEMPLO = 100;
	private static final double IVA_UNIT = 21;
	private static final double EO_UNIT = 66;
	private static final double EI_UNIT = 26.05;
	private static final double IMPUESTOS_ELECTRONICO_UNIT = IVA_UNIT + EO_UNIT;
	private static final double IMPUESTOS_HOGAR_UNIT = IVA_UNIT + EI_UNIT;
	private static final double FINAL_ELECTRONICO_UNIT = PRECIO_BASE_EJEMPLO + IMPUESTOS_ELECTRONICO_UNIT;
	private static final double FINAL_HOGAR_UNIT = PRECIO_BASE_EJEMPLO + IMPUESTOS_HOGAR_UNIT;

	private TipoProducto tipoElectronico;
	private TipoProducto tipoHogar;
    private GeneradorIdSecuencial generadorIdSecuencial;
    private ObservadorSffa observadorSffa;
    private ObservadorSvibaa observadorSvibaa;
    private Comercio comercio;

	@BeforeEach
	void setUp() {
		IVA.setPorcentaje(0.21);
		EO.setFactorPrecioBase(0.5);
		EO.setCoeficienteGanancias(4);
		EO.setGananciasImpositivas(4);
		EI.setDivisorPrecioBase(4);
		EI.setFactorGanancias(0.3);
		EI.setGananciasImpositivas(3.5);

		tipoElectronico = new TipoProducto("Electrónico");
		tipoElectronico.agregarImpuestos(new IVA(), new EO());

		tipoHogar = new TipoProducto("Hogar");
		tipoHogar.agregarImpuestos(new IVA(), new EI());

        generadorIdSecuencial = new GeneradorIdSecuencial();

        observadorSffa = new ObservadorSffa();
        observadorSvibaa = new ObservadorSvibaa();

        comercio = new Comercio(generadorIdSecuencial.siguiente());

        comercio.agregarObservadores(observadorSvibaa, observadorSffa);
	}

	@Test
	void ventaUnProductoElectronico_verificaIvaMasEo() {
		Producto tv = new Producto(null, tipoElectronico, PRECIO_BASE_EJEMPLO, "Smart TV 50");

		Venta venta = new Venta(generadorIdSecuencial.siguiente());
        venta.agregarItem(new ItemVenta(tv, 1));

        comercio.agregarProducto(tv);
        comercio.agregarVenta(venta);

		assertEquals(PRECIO_BASE_EJEMPLO, venta.totalPrecioBase(), 1);
		assertEquals(IMPUESTOS_ELECTRONICO_UNIT, venta.totalImpuestos(), 1);
		assertEquals(FINAL_ELECTRONICO_UNIT, venta.totalFinal(), 1);
        assertEquals(1, observadorSffa.getCantLlamadas());
        assertEquals(1, observadorSvibaa.getCantLlamadas());
	}

	@Test
	void ventaUnProductoHogar_verificaIvaMasEi() {
		Producto cafetera = new Producto(null, tipoHogar, PRECIO_BASE_EJEMPLO, "Cafetera Express");

		Venta venta = new Venta(generadorIdSecuencial.siguiente());
        venta.agregarItem(new ItemVenta(cafetera, 1));

		assertEquals(PRECIO_BASE_EJEMPLO, venta.totalPrecioBase(), 1);
		assertEquals(IMPUESTOS_HOGAR_UNIT, venta.totalImpuestos(), 1);
		assertEquals(FINAL_HOGAR_UNIT, venta.totalFinal(), 1);
	}

	@Test
	void ventaDosProductosElectronicos_impuestosPorProductoYTotal() {
		Producto tv1 = new Producto(null, tipoElectronico, PRECIO_BASE_EJEMPLO, "Smart TV 50");
		Producto tv2 = new Producto(null, tipoElectronico, PRECIO_BASE_EJEMPLO, "Smart TV 50");
		assertEquals(IMPUESTOS_ELECTRONICO_UNIT, tv1.totalImpuestos(), 1);
		assertEquals(IMPUESTOS_ELECTRONICO_UNIT, tv2.totalImpuestos(), 1);

		Venta venta = new Venta(generadorIdSecuencial.siguiente());
        venta.agregarItem(new ItemVenta(tv1, 1));
        venta.agregarItem(new ItemVenta(tv2, 1));

		assertEquals(200, venta.totalPrecioBase(), 1);
		assertEquals(2 * IMPUESTOS_ELECTRONICO_UNIT, venta.totalImpuestos(), 1);
		assertEquals(2 * FINAL_ELECTRONICO_UNIT, venta.totalFinal(), 1);
	}

	@Test
	void ventaMixtaElectronicoYHogar_aplicaImpuestosCorrespondientes() {
		Producto tv = new Producto(null, tipoElectronico, PRECIO_BASE_EJEMPLO, "Smart TV 50");
		Producto cafetera = new Producto(null, tipoHogar, PRECIO_BASE_EJEMPLO, "Cafetera Express");

		Venta venta = new Venta(generadorIdSecuencial.siguiente());
        venta.agregarItem(new ItemVenta(cafetera, 1));
        venta.agregarItem(new ItemVenta(tv, 1));

		assertEquals(200, venta.totalPrecioBase(), 1);
		assertEquals(IMPUESTOS_ELECTRONICO_UNIT + IMPUESTOS_HOGAR_UNIT, venta.totalImpuestos(), 1);
		assertEquals(FINAL_ELECTRONICO_UNIT + FINAL_HOGAR_UNIT, venta.totalFinal(), 1);
	}

}
