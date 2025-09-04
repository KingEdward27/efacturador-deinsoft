
import { ActComprobante } from "@pages/act-comprobante/act-comprobante.model";
import { CnfImpuestoCondicion } from "./cnf-impuesto-condicion.model";
import { CnfProducto } from "./cnf-producto.model";

export class ActComprobanteDetalle {
	id: number = 0;
	descripcion: string = "";
	cantidad!: number;
	precio!: number;
	descuento!: number;
	importe!: number;
	afectacionIgv!: number;
	actComprobante: ActComprobante = new ActComprobante();
	cnfProducto: CnfProducto = new CnfProducto();
	cnfImpuestoCondicion: CnfImpuestoCondicion = new CnfImpuestoCondicion();
	token?: string = "";
};
