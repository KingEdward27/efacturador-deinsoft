import { CnfLocal } from "./cnf-local.model";

export class InvAlmacen {
	id: number = 0;
	nombre: string = "";
	flagEstado: string = "";
	cnfLocal: CnfLocal = new CnfLocal();
	token?: string = "";
};
