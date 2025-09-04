import { CnfRegion } from "./cnf-region.model";


export class CnfProvincia {
	id: number = 0;
	nombre: string = "";
	cnfRegion: CnfRegion = new CnfRegion();
	token?: string = "";
};
