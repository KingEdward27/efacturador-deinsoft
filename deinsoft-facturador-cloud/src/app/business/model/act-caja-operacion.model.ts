
import { ActComprobante } from '@pages/act-comprobante/act-comprobante.model';
import * as dayjs from 'dayjs';
import { ActCajaTurno } from './act-caja-turno.model';
import { ActPago } from './act-pago.model';
import { CnfLocal } from './cnf-local.model';
import { CnfMaestro } from './cnf-maestro.model';

export class ActCajaOperacion {
	id: number = 0;
	monto!: number;
	fecha?: dayjs.Dayjs | null;
	fechaRegistro!: dayjs.Dayjs | null;
	flagIngreso: string = "";
	estado: string = "";
	actCajaTurno: ActCajaTurno = new ActCajaTurno();
	actComprobante: ActComprobante = new ActComprobante();
	actPago: ActPago = new ActPago();
	token?: string = "";
	fechaDesde?: dayjs.Dayjs;
    fechaHasta?: dayjs.Dayjs;
	cnfLocal: CnfLocal = new CnfLocal();
	cnfMaestro: CnfMaestro = new CnfMaestro();
	detail:string;
};
