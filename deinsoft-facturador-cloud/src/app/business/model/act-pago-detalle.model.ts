import { ActPago } from './act-pago.model';
import { ActPagoProgramacion } from './act-pago-programacion.model';

export class ActPagoDetalle {
	id: number = 0;
	montoDeuda!: number;
	monto!: number;
	actPago: ActPago = new ActPago();
	actPagoProgramacion: ActPagoProgramacion = new ActPagoProgramacion();
	token?: string = "";
};
