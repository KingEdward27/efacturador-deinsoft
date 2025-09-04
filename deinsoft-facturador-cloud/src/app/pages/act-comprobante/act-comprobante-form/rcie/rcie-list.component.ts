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
  selector: 'app-rcie-list',
  templateUrl: './rcie-list.component.html',
  providers: [
    { provide: NgbDateAdapter, useClass: CustomAdapter },
    { provide: NgbDateParserFormatter, useClass: CustomDateParserFormatter },
    MyBaseComponentDependences
  ]
})
export class RcieListFormComponent extends CommonReportFormComponent implements OnInit {

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
  selectDefaultCnfMaestro: any = { id: 0, nombre: "- Seleccione -" };
  listCnfMaestro: any;
  cnfMaestro: CnfMaestro = new CnfMaestro();
  loadingCnfMaestro: boolean = false;
  selectDefaultCnfFormaPago: any = { id: 0, nombre: "- Seleccione -" }; listCnfFormaPago: any;
  cnfFormaPago: CnfFormaPago = new CnfFormaPago();
  loadingCnfFormaPago: boolean = false;
  selectDefaultCnfMoneda: any = { id: 0, nombre: "- Seleccione -" }; listCnfMoneda: any;
  cnfMoneda: CnfMoneda = new CnfMoneda();
  loadingCnfMoneda: boolean = false;
  selectDefaultCnfLocal: any = { id: 0, nombre: "- Todos -" }; listCnfLocal: any;
  cnfLocal: CnfLocal = new CnfLocal();
  loadingCnfLocal: boolean = false;
  selectDefaultCnfTipoComprobante: any = { id: 0, nombre: "- Seleccione -" };
  listCnfTipoComprobante: any;
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
  dataTable: any;
  dataTable2: any;

  public modalRef!: NgbModalRef;
  listData: any;
  listDataResumen: any;
  listDataComparacion: any;
  indSituacion: any = 0;
  seleccionAll: boolean = false;
  total: number;
  pendientes: number;
  enviados: number;
  rechazados: number;
  loadingResumen: boolean = false;
  loadingListData: boolean= false;
  loadingListDataComparacion: boolean= false;
  constructor(public deps: MyBaseComponentDependences) {
    super(deps);
  }
  ngOnInit(): void {
    this.isDataLoaded = false;
    this.titleExport = "Reporte de Compras"
    super.ngOnInit();
    this.model.flagIsventa = '2';
    this.model.libro = "080000";
    //this.getListData();
  }

  getListData() {
    if (!this.model.empresa.id) {
      this.deps.utilService.msgWarning("Selección de empresa", "Debe seleccionar la empresa");
      return;
    }

    this.loadingResumen = true;
    this.deps.actComprobanteService.getResumenRcle(this.model).subscribe(data => {
      console.log(data);
      this.listDataResumen = data;
      this.loadingResumen = false;
    }, err => {
      this.deps.utilService.msgHTTP400WithMessage(err.message);
      console.log(err);
    });

    this.loadingListData = true;
    this.deps.actComprobanteService.getPropuestaRcle(this.model).subscribe(data => {
      console.log(data);
      this.listData = data;
      setTimeout(() => {
        this.dataTable = $('#dtDataRcie').DataTable(this.datablesSettingsWithInputs);
      }, 1);
      this.dataTable?.destroy();
      this.loadingListData = false;
    }, err => {
      this.deps.utilService.msgHTTP400WithMessage(err.message);
      console.log(err);
    });

    this.loadingListDataComparacion = true;
    return this.deps.actComprobanteService.getComparacionRcle(this.model).subscribe(data => {
      console.log(data);
      this.listDataComparacion = data;
      setTimeout(() => {
        this.dataTable2 = $('#dtDataRcieCompara').DataTable(this.datablesSettingsWithInputs);
      }, 1);
      this.dataTable2?.destroy();
      this.loadingListDataComparacion = false;
      if (!data){
        this.deps.utilService.msgWarning("Advertencia","No existen datos DE COMPRAS para comparar con SUNAT");
      }
    }, err => {
      this.deps.utilService.msgHTTP400WithMessage(err.message);
      console.log(err);
    });
  }
}

