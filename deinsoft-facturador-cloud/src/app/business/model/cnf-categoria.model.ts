import { CnfEmpresa } from "./cnf-empresa.model";


export class CnfCategoria {
	id: number = 0;
	nombre: string = "";
	flagEstado: string = "";
	cnfEmpresa: CnfEmpresa = new CnfEmpresa();
	token?: string = "";
};
