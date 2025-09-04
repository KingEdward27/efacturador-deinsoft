import { CnfDistrito } from "./cnf-distrito.model";
import { CnfTipoDocumento } from "./cnf-tipo-documento.model";

export class CnfEmpresa {
	id: number = 0;
	nombre: string = "";
	descripcion: string = "";
	nroDocumento: string = "";
	direccion: string = "";
	telefono: string = "";
	empresacol: string = "";
	estado: string = "";
	cnfTipoDocumento: CnfTipoDocumento = new CnfTipoDocumento();
	cnfDistrito: CnfDistrito = new CnfDistrito();
	flagCompraRapida:number;
	flagVentaRapida:number;
	token?: string = "";
};
