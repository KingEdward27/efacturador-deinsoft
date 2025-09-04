
import { ActComprobanteDetalle } from '@/business/model/act-comprobante-detalle.model';
import { CnfFormaPago } from '@/business/model/cnf-forma-pago.model';
import { CnfLocal } from '@/business/model/cnf-local.model';
import { CnfMaestro } from '@/business/model/cnf-maestro.model';
import { CnfMoneda } from '@/business/model/cnf-moneda.model';
import { CnfTipoComprobante } from '@/business/model/cnf-tipo-comprobante.model';
import { InvAlmacen } from '@/business/model/inv-almacen.model';
import * as dayjs from 'dayjs';
import { ActComprobante } from './act-comprobante.model';

export class ActComprobanteCompara extends ActComprobante {
	id: number = 0;
	fecha?: dayjs.Dayjs;
	serie: string = "";
	numero: string = "";
	fechaRegistro!: dayjs.Dayjs;
	billete!: number;
	total!: number;
	vuelto!: number;
	descuento!: number;
	subtotal!: number;
	igv!: number;
	observacion: string = "";
	flagEstado: string = "";
	flagIsventa: string = "";
	envioPseFlag: string = "";
	envioPseMensaje: string = "";
	xmlhash: string = "";
	codigoqr: string = "";
	numTicket: string = "";
	token?: string = "";
	periodo:any;
	
    exists: boolean ;
    samePeriodo: boolean ;
    difIgv: number;
    difTotales: number;
};
