import { Component, HostListener, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import Swal from 'sweetalert2';
import { Observable, of } from 'rxjs';

import { NgbCalendar, NgbDateAdapter, NgbDateParserFormatter, NgbModal, NgbModalConfig, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { CnfLocal } from '@/business/model/cnf-local.model';
import { CnfMoneda } from '@/business/model/cnf-moneda.model';
import { CnfFormaPago } from '@/business/model/cnf-forma-pago.model';
import { CnfMaestro } from '@/business/model/cnf-maestro.model';
import { CnfTipoComprobante } from '@/business/model/cnf-tipo-comprobante.model';
import { InvAlmacen } from '@/business/model/inv-almacen.model';
import { CustomAdapter, CustomDateParserFormatter } from '@/base/util/CustomDate';
import { ActComprobante } from '@pages/act-comprobante/act-comprobante.model';
import { MessageModalComponent } from '@pages/act-comprobante/modal/message-modal.component';
import { CommonReportFormComponent, MyBaseComponentDependences } from '@pages/reports/base/common-report.component';



@Component({
  selector: 'app-act-pago-programacion-report',
  templateUrl: './act-pago-programacion-report.component.html',
  providers: [
    { provide: NgbDateAdapter, useClass: CustomAdapter },
    { provide: NgbDateParserFormatter, useClass: CustomDateParserFormatter },
    MyBaseComponentDependences
  ]
})
export class ActPagoProgramacionReportComponent extends CommonReportFormComponent implements OnInit {

  //generic variables
  error: any;
  selectedItemsList = [];
  checkedIDs = [];
  chargingsb: boolean = true;
  isDataLoaded: Boolean = false;
  isOk: boolean = false;
  isWarning: boolean = false;
  isDanger: boolean = false;
  message: any = "";
  id: string = "";
  searchFailed = false;
  //variables propias
  selectDefaultActComprobante: any = { id: 0, nombre: "- Seleccione -" }; listActComprobante: any;
  actComprobante: ActComprobante = new ActComprobante();
  loadingActComprobante: boolean = false;
  selectDefaultCnfMaestro: any = { id: 0, nombre: "- Seleccione -" }; listCnfMaestro: any;
  cnfMaestro: CnfMaestro = new CnfMaestro();
  loadingCnfMaestro: boolean = false;
  selectDefaultCnfFormaPago: any = { id: 0, nombre: "- Seleccione -" }; listCnfFormaPago: any;
  cnfFormaPago: CnfFormaPago = new CnfFormaPago();
  loadingCnfFormaPago: boolean = false;
  selectDefaultCnfMoneda: any = { id: 0, nombre: "- Seleccione -" }; listCnfMoneda: any;
  cnfMoneda: CnfMoneda = new CnfMoneda();
  loadingCnfMoneda: boolean = false;
  selectDefaultCnfLocal: any = { id: 0, nombre: "- Seleccione -" }; listCnfLocal: any;
  cnfLocal: CnfLocal = new CnfLocal();
  loadingCnfLocal: boolean = false;
  selectDefaultCnfTipoComprobante: any = { id: 0, nombre: "- Seleccione -" }; listCnfTipoComprobante: any;
  cnfTipoComprobante: CnfTipoComprobante = new CnfTipoComprobante();
  loadingCnfTipoComprobante: boolean = false;
  selectDefaultInvAlmacen: any = { id: 0, nombre: "- Seleccione -" }; listInvAlmacen: any;
  invAlmacen: InvAlmacen = new InvAlmacen();
  loadingInvAlmacen: boolean = false;
  listActComprobanteDetalle: any;
  selectedOptionActComprobanteDetalle: any;
  protected redirect: string = "/act-comprobante";
  selectedOption: any;
  passwordRepeat: any;
  isAdding: boolean = false;
  isSave: boolean = false;
  public modelOrig: ActComprobante = new ActComprobante();
  cnfProducto: any;
  formatter = (x: { nombre: string }) => x.nombre;
  dataTable:any;
  loadingCnfImpuestoCondicion: boolean = false;
  selectDefaultImpuestoCondicion: any = { id: 0, nombre: "- Seleccione -" };
  listImpuestoCondicion: any;
  public modalRef!: NgbModalRef;
  listData: any;
  total :any = 0;
  totalMontos :any = 0;
  totalPendiente :any = 0;
  constructor(public deps: MyBaseComponentDependences) {
    super(deps);
  }
  ngOnInit(): void {
    this.isDataLoaded = false;
    // this.model.fechaVencimiento = this.deps.dateAdapter.toModel(this.deps.ngbCalendar.getToday())!.toString();
    
    // this.selectThisMonth();
    this.titleExport = "Lista de Cuentas x Cobrar/x Pagar"
    super.ngOnInit();
    //this.getListData();
  }
//   selectThisMonth() {
//     let year = new Date().getFullYear();
//     let month = new Date().getMonth()+1;
//     let day = new Date(year, month, 0).getDate();
    
//     this.model.fechaVencimiento  = this.deps.dateAdapter
//     .toModel({year: year, month: month, day: day}).toString();
// }
  getListData() {
    this.model.flagIsventa = '1';
    this.totalMontos = 0
    this.totalPendiente = 0
    this.indexInputs = [8]
    this.model.fechaVencimiento = this.model.fechaVencimiento?this.model.fechaVencimiento:''
    return this.deps.actPagoProgramacionService
    .getAllByCnfMaestroId(this.model.cnfMaestro.id,this.model.fechaVencimiento, this.model.cnfLocal.id, false).subscribe(data => {
      this.listData = data;
      this.loadingCnfMaestro = false;
      this.dataTable?.destroy();
      setTimeout(() => {
        this.dataTable = $('#dtDataReportActPagoProgramacion').DataTable(this.datablesSettings);
      }, 1);
      this.listData.forEach(element => {
        this.totalMontos = this.totalMontos + element.monto
        this.totalPendiente = this.totalPendiente + element.montoPendiente
      });
      console.log(data);
    })
  }
}

