import { CnfProvincia } from "./cnf-provincia.model";

export class CnfDistrito {
	id: number = 0;
	nombre: string = "";
	value: string = "";
	cnfProvincia: CnfProvincia = new CnfProvincia();
	token?: string = "";
};
