import { CnfEmpresa } from "./cnf-empresa.model";


export class CnfLocal {
	
	id: number = 0;
	nombre: string = "";
	direccion: string = "";
	cnfEmpresa: CnfEmpresa = new CnfEmpresa();
	token?: string = "";
};
