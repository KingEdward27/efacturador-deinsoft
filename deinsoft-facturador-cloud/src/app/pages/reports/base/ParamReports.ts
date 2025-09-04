
import { CnfCategoria } from '@/business/model/cnf-categoria.model';
import { CnfEmpresa } from '@/business/model/cnf-empresa.model';
import { CnfFormaPago } from '@/business/model/cnf-forma-pago.model';
import { CnfLocal } from '@/business/model/cnf-local.model';
import { CnfMaestro } from '@/business/model/cnf-maestro.model';
import { CnfMoneda } from '@/business/model/cnf-moneda.model';
import { CnfTipoComprobante } from '@/business/model/cnf-tipo-comprobante.model';
import { InvAlmacen } from '@/business/model/inv-almacen.model';
import { StringMap } from '@angular/compiler/src/compiler_facade_interface';
import * as dayjs from 'dayjs';
import { CnfZona } from '../../../business/model/cnf-zona';

export class GenericBean{
	id:number = 0;
	name:string = "TODOS";
}
export class ParamBean {
	id!:number;
	cnfMaestro: CnfMaestro = new CnfMaestro();
	cnfFormaPago: CnfFormaPago = new CnfFormaPago();
	cnfMoneda: CnfMoneda = new CnfMoneda();
	cnfLocal: CnfLocal = new CnfLocal();
	cnfTipoComprobante: CnfTipoComprobante = new CnfTipoComprobante();
	invAlmacen: InvAlmacen = new InvAlmacen();
	fechaDesde?: dayjs.Dayjs;
    fechaHasta?: dayjs.Dayjs;
	flagIsventa:string;
	fechaVencimiento: string;
	empresa:CnfEmpresa = new CnfEmpresa();
	cnfCategoria:CnfCategoria = new CnfCategoria();
	direccion: string;
	periodo: any = {};
	cnfZona:CnfZona = new CnfZona();
	anio: any = "0";
	libro: string;
	codEstadoEnvio: string = '-1';
	flagEstado: string = '1';
};
