export enum State {
    INACTIVO,ACTIVO
  }
export enum Status {
    REGISTRADO = 0,COMPLETADO = 1, ANULADO = 2
}
export enum LimitType {
  DIARIO = 1,SEMANAL = 2, MENSUAL = 3
}  
export enum AgreementType {
  "CONSUMO INTERNO" = 0,CREDITO = 1, "PAGO ANTICIPADO" = 2, "VENTA ADELANTADA" = 3, "PAGO EN EFECTIVO" = 4
}  
export enum AuthorizationType {
  PLACA = 1,"TARJETA MAGNETICA" = 2, CERTIFICADO = 3, VIS = 4, IBUTTON = 5, ANILLO = 6, "Tag RFID" = 7
}  
export class Generic {
  id:number = 0;
  description!:string;
}