import dayjs from "dayjs";
import { CnfDistrito } from "./cnf-distrito.model";
import { CnfEmpresa } from "./cnf-empresa.model";
import { CnfTipoDocumento } from "./cnf-tipo-documento.model";


export class CnfMaestro {
	
	id: number = 0;
	nroDoc: string = "";
	nombres: string = "";
	apellidoPaterno: string = "";
	apellidoMaterno: string = "";
	razonSocial: string = "";
	direccion: string = "";
	correo: string = "";
	telefono: string = "";
	fechaRegistro!: dayjs.Dayjs;
	flagEstado: string = "";
	cnfTipoDocumento: CnfTipoDocumento = new CnfTipoDocumento();
	cnfEmpresa: CnfEmpresa = new CnfEmpresa();
	cnfDistrito: CnfDistrito = new CnfDistrito();
	token?: string = "";
};
