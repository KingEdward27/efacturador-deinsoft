import { CnfLocal } from "./cnf-local.model";
import { CnfTipoComprobante } from "./cnf-tipo-comprobante.model";

export class CnfNumComprobante {
	id: number = 0;
	serie: string = "";
	ultimoNro!: number;
	cnfTipoComprobante: CnfTipoComprobante = new CnfTipoComprobante();
	cnfLocal: CnfLocal = new CnfLocal();
	token?: string = "";
};
