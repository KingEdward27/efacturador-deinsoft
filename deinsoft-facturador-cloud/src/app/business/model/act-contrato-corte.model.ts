
import { ActContrato } from '@pages/act-contrato/act-contrato.model';
import * as dayjs from 'dayjs';
import { SegUsuario } from './seg-usuario.model';

export class ActContratoCorte {
	id: number = 0;
	fecha?: dayjs.Dayjs | null;
	flgEstado: string = "";
	actContrato: ActContrato = new ActContrato();
	segUsuario: SegUsuario = new SegUsuario();
	observacion: string = "";
	token?: string = "";
};
