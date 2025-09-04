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
import { CnfMaestroService } from '@/business/service/cnf-maestro.service';
import { CnfFormaPagoService } from '@/business/service/cnf-forma-pago.service';
import { CnfMonedaService } from '@/business/service/cnf-moneda.service';
import { CnfLocalService } from '@/business/service/cnf-local.service';
import { CnfTipoComprobanteService } from '@/business/service/cnf-tipo-comprobante.service';
import { InvAlmacenService } from '@/business/service/inv-almacen.service';
import { ActComprobanteDetalleService } from '@/business/service/act-comprobante-detalle.service';
import { UtilService } from '@services/util.service';
import { CustomAdapter, CustomDateParserFormatter } from '@/base/util/CustomDate';
import { catchError, debounceTime, distinctUntilChanged, switchMap, tap, filter } from 'rxjs/operators';
import { CnfProductoService } from '@/business/service/cnf-producto.service';
import { CnfProducto } from '@/business/model/cnf-producto.model';
import { ActComprobanteDetalle } from '@/business/model/act-comprobante-detalle.model';
import { CnfImpuestoCondicion } from '@/business/model/cnf-impuesto-condicion.model';
import { CnfImpuestoCondicionService } from '@/business/service/cnf-impuesto-condicion.service';
import { CnfNumComprobanteService } from '@/business/service/cnf-num-comprobante.service';
import { CommonService } from '@/base/services/common.service';
import { UpdateParam } from '@/base/components/model/UpdateParam';
import dayjs from 'dayjs';
import { AppService } from '@services/app.service';
import { ActComprobante } from '@pages/act-comprobante/act-comprobante.model';
import { MessageModalComponent } from '@pages/act-comprobante/modal/message-modal.component';
import { CommonReportFormComponent, MyBaseComponentDependences } from '@pages/reports/base/common-report.component';
import { SegUsuario } from '../../../../business/model/seg-usuario.model';



@Component({
  selector: 'app-rvie-list',
  templateUrl: './rvie-list.component.html',
  providers: [
    { provide: NgbDateAdapter, useClass: CustomAdapter },
    { provide: NgbDateParserFormatter, useClass: CustomDateParserFormatter },
    MyBaseComponentDependences
  ]
})
export class RvieListFormComponent extends CommonReportFormComponent implements OnInit {

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
  listDataVentas: any;
  listDataComparacion: any;
  indSituacion: any = 0;
  seleccionAll: boolean = false;
  total: number;
  pendientes: number;
  enviados: number;
  rechazados: number;
  listDataResumen: any;

  loadingResumen: boolean = false;
  loadingListData: boolean = false;
  loadingListDataComparacion: boolean = false;
  
  contadorExists: number= 0
  contadorPeriodo: number= 0
  diferenciaIgv: number= 0
  diferenciaTotal: number= 0
  constructor(public deps: MyBaseComponentDependences) {
    super(deps);
  }
  ngOnInit(): void {
    this.isDataLoaded = false;
    this.titleExport = "Reporte de Ventas"
    super.ngOnInit();
    console.log(this.model);
    this.model.flagIsventa = '1';
    this.model.libro = "140000";
    //this.getListData();
  }

  getListData() {
    if (!this.model.empresa.id) {
      this.deps.utilService.msgWarning("Selección de empresa", "Debe seleccionar la empresa");
      return;
    }
    console.log(this.model);

    this.loadingResumen = true;
    this.deps.actComprobanteService.getResumenRcle(this.model).subscribe(data => {
      console.log(data);
      this.listDataResumen = data;
      this.loadingResumen = false;

    }, err => {
      this.deps.utilService.msgHTTP400WithMessage(err.message);
      console.log(err);
    });

    this.getListDataDetail();

  }
  getListDataDetail() {
    this.loadingListData = true;
    this.deps.actComprobanteService.getPropuestaRcle(this.model).subscribe(data => {
      console.log(data);
      this.listDataVentas = data;
      setTimeout(() => {
        this.dataTable = $('#dtDataRvie').DataTable(this.datablesSettingsWithInputs);
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
      this.contadorExists = 0;
      this.contadorPeriodo = 0;
      this.diferenciaIgv = 0;
      this.diferenciaTotal = 0;
      setTimeout(() => {
        this.dataTable2 = $('#dtDataRcieComparaRvie').DataTable(this.datablesSettingsWithInputs);
        this.listDataComparacion.forEach(element => {
          if (element.exists) {
            this.contadorExists = this.contadorExists + 1;
          }
          if (element.samePeriodo) {
            this.contadorPeriodo = this.contadorPeriodo + 1;
          }
          this.diferenciaIgv = this.diferenciaIgv + element.difIgv
          this.diferenciaTotal = this.diferenciaTotal + element.difTotales
        });
      }, 1);
      this.dataTable2?.destroy();
      this.loadingListDataComparacion = false;
    }, err => {
      this.deps.utilService.msgHTTP400WithMessage(err.message);
      console.log(err);
    });
  }

  aceptarPropuesta() {
    this.deps.utilService.msgWarning("Aviso","Opción en desarrollo");
  }
}

