import { SegAccion } from "./seg-accion.model";
import { SegMenu } from "./seg-menu.model";
import { SegRol } from "./seg-rol.model";

export class SegPermiso {
	id: number = 0;
	segRol: SegRol = new SegRol();
	segMenu: SegMenu = new SegMenu();
	segAccion: SegAccion = new SegAccion();
	token?: string = "";
};
