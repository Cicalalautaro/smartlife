package ar.edu.utn.ba.ddsi.smartlife.sales_service.models.entities.observers;

import ar.edu.utn.ba.ddsi.smartlife.sales_service.models.entities.venta.Venta;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ObservadorSffa implements ObservadorVenta {
    @Getter
    private long cantLlamadas;

	@Override
	public void serNotificadoDe(Venta venta) {
        //TODO: pendiente de integración real
		cantLlamadas++;
	}

}
