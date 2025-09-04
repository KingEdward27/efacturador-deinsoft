import { CnfProducto } from "./cnf-producto.model";
import { InvMovAlmacen } from "./inv-mov-almacen.model";

export class InvMovAlmacenDet {
	id: number = 0;
	cantidad!: number;
	precio!: number;
	importe!: number;
	nroserie: string = "";
	invMovAlmacen: InvMovAlmacen = new InvMovAlmacen();
	cnfProducto: CnfProducto = new CnfProducto();
	token?: string = "";
};
