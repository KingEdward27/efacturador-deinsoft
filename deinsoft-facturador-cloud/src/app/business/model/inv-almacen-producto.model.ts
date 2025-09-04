import { CnfProducto } from "./cnf-producto.model";
import { InvAlmacen } from "./inv-almacen.model";

export class InvAlmacenProducto {
	id: number = 0;
	cantidad!: number;
	invAlmacen: InvAlmacen = new InvAlmacen();
	cnfProducto: CnfProducto = new CnfProducto();
	token?: string = "";
};
