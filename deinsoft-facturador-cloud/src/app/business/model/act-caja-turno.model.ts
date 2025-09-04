import dayjs from "dayjs";
import { ActCaja } from "./act-caja.model";
import { SegUsuario } from "./seg-usuario.model";

export class ActCajaTurno {
	id: number = 0;
	fechaApertura!: string;
	fechaCierre!: string;
	montoApertura:number
	montoCierre:number
	estado: string = "";
	segUsuario: SegUsuario = new SegUsuario();
	actCaja:ActCaja = new ActCaja();
	token?: string = "";
};
