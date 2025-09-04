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
import { environment } from 'environments/environment';



@Component({
  selector: 'app-act-comprobante-list',
  templateUrl: './act-comprobante-list.component.html',
  providers: [
    { provide: NgbDateAdapter, useClass: CustomAdapter },
    { provide: NgbDateParserFormatter, useClass: CustomDateParserFormatter },
    MyBaseComponentDependences
  ]
})
export class ActComprobanteListFormComponent extends CommonReportFormComponent implements OnInit {

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
  dataTable:any;
  
  public modalRef!: NgbModalRef;
  listData: any;
  indSituacion:string = '-1';
  estado:any = '1';
  seleccionAll:boolean = false;
  total:number;
  pendientes:number;
  enviados:number;
  rechazados:number;
  invalidos:number;
  constructor(public deps: MyBaseComponentDependences) {
    super(deps);
    this.deps.commonService.baseEndpoint = environment.apiUrl;
  }
  ngOnInit(): void {
    this.isDataLoaded = false;
    this.titleExport = "Reporte de Ventas"
    super.ngOnInit();
    //this.getListData();
  }
  
  
  getListData() {
    this.model.flagIsventa = '1';
    if  (!this.model.empresa.id){
      this.deps.utilService.msgWarning("Selección de empresa","Debe seleccionar la empresa");
      return;
    }
    return this.deps.actComprobanteService.getReport(this.model).subscribe(data => {
      this.listData = data;
      this.loadingCnfMaestro = false;
      console.log(data);
      
      this.indexInputs = [1,11]
      let pendientes = this.listData.filter(data => data.indSituacion !== '03' && data.estado == '1')
      
      
      if (pendientes.length > 0) {
        this.error = "Hay comprobantes pendientes de envío o rechazados (Cantidad: "+pendientes.length+"). Consultar con Administrador TI"
      }

      // if(this.indSituacion === 2 || this.indSituacion === 3){
      //   this.listData = this.listData.filter(data => data.indSituacion === "0" + this.indSituacion)
      // } else if (this.indSituacion !== 0) {
      //   this.listData = this.listData.filter(data => data.indSituacion !== "02" && data.indSituacion !== "03" )
      // }
      this.total = this.listData.filter(data => data.indSituacion !== "00").length;
      this.pendientes = this.listData.filter(data => (data.indSituacion === "01" || data.indSituacion === "02") && data.estado == '1')?.length;
      this.enviados = this.listData.filter(data => data.indSituacion === "03")?.length;
      this.rechazados = this.listData.filter(data => (data.indSituacion === "06" || data.indSituacion === "10"))?.length;
      setTimeout(() => {
        this.dataTable = $('#dtData').DataTable(this.datablesSettingsWithInputs);
      }, 1);
      this.dataTable?.destroy();
    })
  }
  selectAll(){
    this.seleccionAll = !this.seleccionAll;
    this.listData.forEach(element => {
      element.selection = this.seleccionAll;
    });
    
  }
  print(item: any) {
    this.modalRef = this.deps.modalService.open(MessageModalComponent);
    this.modalRef.componentInstance.message = "Impresión de comprobante";
    this.modalRef.componentInstance.id = item.id;

  }
  resumenDiario() {
    this.deps.utilService.confirmOperation(null).then((result) => {
      if (result) {
        let ids = "";
        for (let index = 0; index < this.listData.length; index++) {
          const element = this.listData[index];
          if (element.selection) {
            ids = ids + element.id + ',';
          }
        }
        ids = ids.substring(0,ids.length-1)
        this.deps.actComprobanteService.resumenDiario(ids).subscribe(data => {
          if(data.status !== 'ACEPTADO') this.deps.utilService.msgHTTP400WithMessage(data.description);
          this.deps.utilService.msgOkOperation();
          this.getListData();
        }, err => {
          this.deps.utilService.msgHTTP400WithMessage(err.message);
          console.log(err);
        });
      }

    });
  }
  genXml(item: any) {
    this.deps.utilService.confirmOperation(null).then((result) => {
      if (result) {
        this.deps.actComprobanteService.genXml(item.id.toString()).subscribe(data => {
          if(data.status !== 'ACEPTADO') this.deps.utilService.msgHTTP400WithMessage(data.description);
          this.deps.utilService.msgOkOperation();
          this.getListData();
        }, err => {

          //error en validaciones, los demas errores en JwtInterceptor
          this.deps.utilService.msgHTTP400WithMessage(err.message);
          console.log(err);
        });
      }

    });
  }

  genNotaCredito(item: any) {
    this.deps.utilService.confirmOperation("GENERAR NOTA DE CREDITO").then((result) => {
      if (result) {
        this.deps.actComprobanteService.genNotaCredito(
          item.serie,item.numero,item.empresa.id).subscribe(data => {
          if(data.status !== 'ACEPTADO') this.deps.utilService.msgHTTP400WithMessage(data.description);
          this.deps.utilService.msgOkOperation();
          this.getListData();
        }, err => {

          //error en validaciones, los demas errores en JwtInterceptor
          this.deps.utilService.msgHTTP400WithMessage(err.message);
          console.log(err);
        });
      }

    });
  }
  sendApi(item: any) {
    this.deps.utilService.confirmOperation(null).then((result) => {
      if (result) {
        this.deps.actComprobanteService.sendApi(item.id.toString()).subscribe(data => {
          if(data.status !== 'ACEPTADO') this.deps.utilService.msgHTTP400WithMessage(data.description);
          this.deps.utilService.msgOkOperation();
          this.getListData();
        }, err => {

          //error en validaciones, los demas errores en JwtInterceptor
          this.deps.utilService.msgHTTP400WithMessage(err.message);
          console.log(err);
        });
      }

    });
  }

  validate(item: any) {
    this.deps.utilService.confirmOperation(null).then((result) => {
      if (result) {
        this.deps.actComprobanteService.validateApi(item.id.toString()).subscribe(data => {
          console.log(data);
          
        }, err => {

          //error en validaciones, los demas errores en JwtInterceptor
          this.deps.utilService.msgHTTP400WithMessage(err.message);
          console.log(err);
        });
      }

    });
  }

  a4() {
    let myMap = new Map();
    myMap.set("id", this.id);
    myMap.set("tipo", 1);
    let mp = new UpdateParam();
    const convMapDetail: any = {};
    myMap.forEach((val: string, key: string) => {
      convMapDetail[key] = val;
    });
    console.log(convMapDetail);

    mp.map = convMapDetail;
    this.deps.commonService.genericPostRequest("/backend/get-xml?id="+this.id, mp, 'blob').subscribe(data => {
      console.log(data);
      if (data.type != 'application/json') {
        var contentType = 'application/pdf';
        var extension = "pdf";

        if (data.type == "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") {
          contentType = data.type;
          extension = "xlsx";
        }
        const blob = new Blob([data], { type: contentType });
        this.generateAttachment(blob, extension);
      }

    });
  }

  xml(item: any): void {
    const requestFileName = 'ejemplo.pdf' ; // nombre a pedir al backend
    
    this.deps.commonService.genericPostRequest("/backend/get-xml?id="+item.id,null, 'json').subscribe({
      next: response => {
        console.log(response);
        
        const fileData = response.xmlBase64!;
        const fileName = response.fileName!;

        const byteCharacters = atob(fileData);
        const byteNumbers = new Array(byteCharacters.length);

        for (let i = 0; i < byteCharacters.length; i++) {
          byteNumbers[i] = byteCharacters.charCodeAt(i);
        }

        const byteArray = new Uint8Array(byteNumbers);
        const blob = new Blob([byteArray], { type: 'application/xml' });

        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = fileName;
        a.click();
        window.URL.revokeObjectURL(url);
      }
    });
  }

  // ticketChild2(){
  //   let myMap = new Map();
  //   myMap.set("id", this.id);
  //   myMap.set("tipo", 2);
  //   let mp = new UpdateParam();
  //   const convMapDetail: any = {};
  //   myMap.forEach((val: string, key: string) => {
  //     convMapDetail[key] = val;
  //   });
  //   console.log(convMapDetail);
  //   mp.map = convMapDetail;
  //   this.deps.commonService.updateParam = mp;
    
  //   this.deps.commonService.genericPostRequest("/backend/get-xml?ID="+this.id, mp, 'json').subscribe(data => {
  //     console.log(data);
      
  //     const payload = {
  //       base64Pdf: data.archivoBase64,
  //       printerName: data.impresora,
  //       pdf:data.pdf
  //     };

  //     this.commonService.genericPostRequestFullWithoutResponseType("http://localhost:7979/punto-venta-client/api/v1/print/ticket", payload).subscribe(data => {
  //       console.log(data);
        
  //     }, err => {
  //       console.log(err);
        
  //       this.utilService.msgError(err.error);
  //     });
  //   }, err => {
  //     console.log(err);
      
  //     this.utilService.msgError(err.error);
  //   });
  // }
}

