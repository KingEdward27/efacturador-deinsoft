import { CnfCategoria } from "./cnf-categoria.model";
import { CnfEmpresa } from "./cnf-empresa.model";

export class CnfSubCategoria {
	id: number = 0;
	nombre: string = "";
	flagEstado: string = "";
	cnfCategoria: CnfCategoria = new CnfCategoria();
	cnfEmpresa: CnfEmpresa = new CnfEmpresa();
	token?: string = "";
};
