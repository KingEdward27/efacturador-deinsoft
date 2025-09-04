import dayjs from "dayjs";
import { CnfEmpresa } from "./cnf-empresa.model";
import { CnfMarca } from "./cnf-marca.model";
import { CnfSubCategoria } from "./cnf-sub-categoria.model";
import { CnfUnidadMedida } from "./cnf-unidad-medida.model";

export class CnfProducto {
	id: number = 0;
	codigo: string = "";
	nombre: string = "";
	precio!: number;
	existencia!: number;
	fechaRegistro!: dayjs.Dayjs;
	rutaImagen: string = "";
	flagEstado: string = "";
	barcode: string = "";
	cnfUnidadMedida: CnfUnidadMedida = new CnfUnidadMedida();
	cnfEmpresa: CnfEmpresa = new CnfEmpresa();
	cnfSubCategoria: CnfSubCategoria = new CnfSubCategoria();
	cnfMarca: CnfMarca = new CnfMarca();
	token?: string = "";
};
