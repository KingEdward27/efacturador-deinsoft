import { ActComprobante } from '@pages/act-comprobante/act-comprobante.model';
import * as dayjs from 'dayjs';

export class ActPagoProgramacion {
	id: number = 0;
	fecha?: dayjs.Dayjs | null;
	fechaVencimiento?: dayjs.Dayjs | null;
	monto!: number;
	montoPendiente!: number;
	actComprobante: ActComprobante = new ActComprobante();
	token?: string = "";
	amtToPay!:number;
};
