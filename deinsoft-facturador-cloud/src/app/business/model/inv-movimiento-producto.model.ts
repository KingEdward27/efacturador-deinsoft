import { ActComprobante } from '@pages/act-comprobante/act-comprobante.model';
import * as dayjs from 'dayjs';
import { CnfProducto } from './cnf-producto.model';
import { InvAlmacen } from './inv-almacen.model';

export class InvMovimientoProducto {
	id: number = 0;
	fecha?: dayjs.Dayjs | null;
	fechaRegistro!: dayjs.Dayjs | null;
	cantidad!: number;
	invAlmacen: InvAlmacen = new InvAlmacen();
	cnfProducto: CnfProducto = new CnfProducto();
	actComprobante: ActComprobante = new ActComprobante();
	costoTotal!: number;
	token?: string = "";
};
