
import * as dayjs from 'dayjs';
import { CnfLocal } from './cnf-local.model';
import { CnfMaestro } from './cnf-maestro.model';
import { CnfTipoComprobante } from './cnf-tipo-comprobante.model';
import { InvAlmacen } from './inv-almacen.model';
import { InvMovAlmacenDet } from './inv-mov-almacen-det.model';
import { InvTipoMovAlmacen } from './inv-tipo-mov-almacen.model';

export class InvMovAlmacen {
	id: number = 0;
	serie: string = "";
	numero: string = "";
	numeroRef: string = "";
	fecha?: dayjs.Dayjs | null;
	observacion: string = "";
	subtotal!: number;
	igv!: number;
	total!: number;
	fechareg?: dayjs.Dayjs | null;
	flagEstado: string = "";
	invTipoMovAlmacen: InvTipoMovAlmacen = new InvTipoMovAlmacen();
	cnfMaestro: CnfMaestro = new CnfMaestro();
	cnfLocal: CnfLocal = new CnfLocal();
	cnfTipoComprobante: CnfTipoComprobante = new CnfTipoComprobante();
	invAlmacen: InvAlmacen = new InvAlmacen();
	listInvMovAlmacenDet: InvMovAlmacenDet[] = [];
	token?: string = "";
};
