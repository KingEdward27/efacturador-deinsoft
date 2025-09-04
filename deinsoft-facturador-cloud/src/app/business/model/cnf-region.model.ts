import { CnfPais } from "./cnf-pais.model";


export class CnfRegion {
	id: number = 0;
	nombre: string = "";
	cnfPais: CnfPais = new CnfPais();
	token?: string = "";
};
