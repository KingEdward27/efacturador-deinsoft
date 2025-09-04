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
import { catchError, debounceTime, distinctUntilChanged, switchMap, tap } from 'rxjs/operators';
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
import { ActComprobante } from '../../act-comprobante.model';
import { ActComprobanteService } from '../../act-comprobante.service';
import { MessageModalComponent } from '../../modal/message-modal.component';
import { Location } from '@angular/common';



@Component({
  selector: 'app-act-comprobante-form',
  templateUrl: './act-comprobante-form.component.html',
  providers: [
    { provide: NgbDateAdapter, useClass: CustomAdapter },
    { provide: NgbDateParserFormatter, useClass: CustomDateParserFormatter }
  ]
})
export class ActComprobanteFormComponent implements OnInit {

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
  public model: ActComprobante = new ActComprobante();
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
  selectDefaultCnfLocal: any = { id: 0, nombre: "- Seleccione -" }; 
  listCnfLocal: any;
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
  formatterCnfMaestro = (x: CnfMaestro ) => (x.apellidoPaterno + ' '+x.apellidoMaterno+ ' '+x.nombres).trim();

  loadingCnfImpuestoCondicion: boolean = false;
  selectDefaultImpuestoCondicion: any = { id: 0, nombre: "- Seleccione -" };
  listImpuestoCondicion: any;
  selectedCnfMaestro: any;
  public modalRef!: NgbModalRef;
  option:string = "1";

  empresaPrincipal: any
  constructor(private actComprobanteService: ActComprobanteService,
    private router: Router,
    private utilService: UtilService,
    private cnfMaestroService: CnfMaestroService,
    private cnfFormaPagoService: CnfFormaPagoService,
    private cnfMonedaService: CnfMonedaService,
    private cnfLocalService: CnfLocalService,
    private cnfTipoComprobanteService: CnfTipoComprobanteService,
    private invAlmacenService: InvAlmacenService,
    private actComprobanteDetalleService: ActComprobanteDetalleService,
    private cnfProductoService: CnfProductoService,
    private cnfImpuestoCondicionService: CnfImpuestoCondicionService,
    private cnfNumComprobanteService: CnfNumComprobanteService,
    private commonService: CommonService,
    private dateAdapter: NgbDateAdapter<dayjs.Dayjs>,
    private ngbCalendar: NgbCalendar,
    private modalService: NgbModal, private _location: Location,
    private route: ActivatedRoute, config: NgbModalConfig,private appService:AppService) {
    config.backdrop = 'static';
    config.keyboard = false;
  }
  ngOnInit(): void {
    this.isDataLoaded = false;
    // $(document).on('click', '#ticket', function () {
    //   this.ticketChild();
    // });
    // $(document).on('click', '#a4', function () {
    //   this.a4();
    // });
    this.loadData();
    let empresaPrincipal = this.appService.getUser().empresaPrincipal
    this.empresaPrincipal = empresaPrincipal
    
    // $(document).on("keyup", function(event) {
    //   if (event.enter) {
    //     document.getElementById("myOptions").focus();
    //   }
    // });
  }
  getBack() {
    this._location.back();
    // this.router.navigate([this.redirect]);
  }
  loadData() {
    this.getListCnfMaestro();
    this.getListCnfFormaPago();
    this.getListCnfMoneda();
    this.getListCnfLocal();
    this.getListCnfTipoComprobante();
    // this.getListInvAlmacen();
    this.getListImpuestoCondicion();
    console.log(this.listCnfLocal);
    
    
    


    this.model.total = 0;
    this.model.subtotal = 0;
    this.model.igv = 0;
    this.model.descuento = 0;
    this.model.fecha = this.dateAdapter.toModel(this.ngbCalendar.getToday())!;
    this.model.flagEstado = "1";
    this.model.flagIsventa = '1';
    return this.route.paramMap.subscribe(params => {
      this.id = params.get('id')!;
      this.option = params.get('option')!;
      console.log(this.id);
      if (!this.id) {
        this.option = "1";
        this.isDataLoaded = true;
      }
      if (this.id) {
        // this.actComprobanteService.getData(this.id).subscribe(data => {
        //   this.model = data;
        //   console.log(this.model);
        //   this.isDataLoaded = true;
        //   //this.titulo = 'Editar ' + this.nombreModel;
        // });
      }

    })

  }
  getListCnfProductAsObservable(term: any): Observable<any> {

    if (term.length >= 2) {
      console.log(this.model.invAlmacen.id);
      let cnfEmpresa = this.appService.getProfile().profile.split("|")[1];  
      return this.cnfProductoService.getAllDataComboTypeHead(term, cnfEmpresa)
        .pipe(
          tap(() => this.searchFailed = false),
          catchError((err: any) => {
            console.log(err);
            this.searchFailed = true;
            return of([]);
          })
        );
    } else {
      return <any>[];
    }

  }
  searchProduct = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      switchMap(term => {
        return this.getListCnfProductAsObservable(term);
      })
    )
  
  onchangeProduct(event: any, input: any) {
    event.preventDefault();
    let actComprobanteDetalle = new ActComprobanteDetalle();
    actComprobanteDetalle.cnfProducto = event.item
    actComprobanteDetalle.descripcion = event.item.nombre
    actComprobanteDetalle.cantidad = 1
    actComprobanteDetalle.cnfImpuestoCondicion.id = 1
    actComprobanteDetalle.precio = event.item.precio
    actComprobanteDetalle.importe = event.item.precio
    actComprobanteDetalle.cnfImpuestoCondicion.id = 1
    actComprobanteDetalle.afectacionIgv
      = actComprobanteDetalle.importe - actComprobanteDetalle.importe / 1.18

    this.model.total = Math.round((this.model.total + event.item.precio + Number.EPSILON) * 100) / 100;
    this.model.subtotal = Math.round((this.model.total / 1.18 + Number.EPSILON) * 100) / 100;
    this.model.descuento = 0
    this.model.igv = Math.round((this.model.total - this.model.subtotal + Number.EPSILON) * 100) / 100
    this.model.listActComprobanteDetalle.push(actComprobanteDetalle);
    input.value = '';
  }
  agregarActComprobanteDetalle(): void {
    this.router.navigate(["/add-new-act-comprobante-detalle", { idParent: this.model.id }]);
  }
  editarActComprobanteDetalle(actComprobanteDetalle: any): void {
    this.router.navigate(["/add-new-act-comprobante-detalle", { idParent: this.model.id, id: actComprobanteDetalle.id }]);
  }
  quitarActComprobanteDetalle(e: any): void {
    if (!this.id) {
      this.model.listActComprobanteDetalle = 
      this.model.listActComprobanteDetalle.filter(item => item.id != e.id);
    }
    if (this.id) {
      this.utilService.confirmDelete(e).then((result) => {
        if (result) {
          this.actComprobanteDetalleService.delete(e.id.toString()).subscribe(() => {
            this.utilService.msgOkDelete();
            this.loadData();
          }, err => {
            if (err.status === 500 && err.error.trace.includes("DataIntegrityViolationException")) {
              this.utilService.msgProblemDelete();
            }
          });
        }

      });
    }
    this.updateTotals()
  }
  save() {
    console.log(this.model);
    if (this.model.id > 0 && this.model.flagEstado != "1") {
      this.utilService.msgHTTP400WithMessage("No pude hacer modificaciones. La compra no está en estado Registrado");
      return;
    }
    this.model.numero = "1"
    if (this.model.listActComprobanteDetalle.length == 0) {
      this.error = []
      this.error.push("Debe agregar productos y servicios al comprobante")
      return;
    }
    // this.actComprobanteService.save(this.model).subscribe(m => {
    //   console.log(m);
    //   this.model.id = m.id
    //   this.model.numero = m.numero;

    //   this.modalRef = this.modalService.open(MessageModalComponent);
    //   this.modalRef.componentInstance.message = "Documento " + m.serie + " - " + m.numero + " generado correctamente";
    //   this.modalRef.componentInstance.id = m.id;
    //   this.modalRef.closed.subscribe(result => {
    //     this.router.navigate(["/venta"]);
    //     // this.model.cnfBpartner = 
    //   })
    // }, err => {
    //   if (err.status === 422) {
    //     this.error = []
    //     console.log(err.error);

    //     for (var prop in err.error) {
    //       // console.log("Key:" + prop);
    //       // console.log("Value:" + err.error[prop]);
    //       this.error.push(err.error[prop])
    //     }
    //     // this.error.Results.forEach(element => {
    //     //   this.error = this.error + element + "/n"
    //     // })
    //     // console.log(this.error);
    //   }
    // });
  }
  compareActComprobante(a1: ActComprobante, a2: ActComprobante): boolean {
    if (a1 === undefined && a2 === undefined) {
      return true;
    }

    return (a1 === null || a2 === null || a1 === undefined || a2 === undefined)
      ? false : a1.id === a2.id;
  }
  getListCnfMaestro() {
    this.loadingCnfMaestro = true;
    let cnfEmpresa = this.appService.getProfile().profile.split("|")[1];
    if(cnfEmpresa == '*') {
      return this.cnfMaestroService.getAllDataCombo().subscribe(data => {
        this.listCnfMaestro = data;
        this.loadingCnfMaestro = false;
      })
    }else{
      return this.cnfMaestroService.getAllByCnfEmpresaId(cnfEmpresa).subscribe(data => {
        this.listCnfMaestro = data;
        this.loadingCnfMaestro = false;
      })
    }
    

  }
  getListImpuestoCondicion() {
    console.log(this.chargingsb);
    this.loadingCnfImpuestoCondicion = true;
    return this.cnfImpuestoCondicionService.getAllDataCombo().subscribe(data => {
      this.listImpuestoCondicion = data;
      this.loadingCnfImpuestoCondicion = false;
    })

  }
  compareCnfImpuestoCondicion(a1: CnfImpuestoCondicion, a2: CnfImpuestoCondicion): boolean {
    if (a1 === undefined && a2 === undefined) {
      return true;
    }

    return (a1 === null || a2 === null || a1 === undefined || a2 === undefined)
      ? false : a1.id === a2.id;
  }
  compareCnfMaestro(a1: CnfMaestro, a2: CnfMaestro): boolean {
    if (a1 === undefined && a2 === undefined) {
      return true;
    }

    return (a1 === null || a2 === null || a1 === undefined || a2 === undefined)
      ? false : a1.id === a2.id;
  }
  getListCnfFormaPago() {
    this.loadingCnfFormaPago = true;
    console.log(this.chargingsb);
    let cnfEmpresa = this.appService.getProfile().profile.split("|")[1];
    if(cnfEmpresa == '*') {
      return this.cnfFormaPagoService.getAllDataCombo().subscribe(data => {
        this.listCnfFormaPago = data;
        this.loadingCnfFormaPago = false;
      })
    }else{
      return this.cnfFormaPagoService.getAllByCnfEmpresaId(cnfEmpresa).subscribe(data => {
        this.listCnfFormaPago = data;
        this.loadingCnfFormaPago = false;
      })
    }
    

  }
  compareCnfFormaPago(a1: CnfFormaPago, a2: CnfFormaPago): boolean {
    if (a1 === undefined && a2 === undefined) {
      return true;
    }

    return (a1 === null || a2 === null || a1 === undefined || a2 === undefined)
      ? false : a1.id === a2.id;
  }
  getListCnfMoneda() {
    this.loadingCnfMoneda = true;
    console.log(this.chargingsb);
    return this.cnfMonedaService.getAllDataCombo().subscribe(data => {
      console.log(data);
      this.listCnfMoneda = data;
      this.loadingCnfMoneda = false;
      this.model.cnfMoneda = this.empresaPrincipal.cnfMoneda
    })

  }
  compareCnfMoneda(a1: CnfMoneda, a2: CnfMoneda): boolean {
    if (a1 === undefined && a2 === undefined) {
      return true;
    }

    return (a1 === null || a2 === null || a1 === undefined || a2 === undefined)
      ? false : a1.id === a2.id;
  }
  getListCnfLocal() {
    this.loadingCnfLocal = true;
    let user = this.appService.getProfile();
    let cnfEmpresa = this.appService.getProfile().profile.split("|")[1];
    if(cnfEmpresa == '*') {
      return this.cnfLocalService.getAllDataCombo().subscribe(data => {
        this.listCnfLocal = data;
        this.loadingCnfLocal = false;
      })
    }else{
      return this.cnfLocalService.getAllByCnfEmpresaId(cnfEmpresa).subscribe(data => {
        this.listCnfLocal = data;
        this.loadingCnfLocal = false;

        
        if(this.listCnfLocal.length == 1) {
          this.cnfLocalService.getData(this.listCnfLocal[0].id).subscribe(data => {
            this.model.cnfLocal = data
            this.getListInvAlmacen()
            
          })
          // this.model.cnfLocal.id = this.listCnfLocal[0].id;
          // if (this.listInvAlmacen.length == 1){
          //   this.model.invAlmacen.id = this.listInvAlmacen[0].id;
          // }
        }
      })
    }
    

  }
  compareCnfLocal(a1: CnfLocal, a2: CnfLocal): boolean {
    if (a1 === undefined && a2 === undefined) {
      return true;
    }

    return (a1 === null || a2 === null || a1 === undefined || a2 === undefined)
      ? false : a1.id === a2.id;
  }
  getListCnfTipoComprobante() {
    this.loadingCnfTipoComprobante = true;
    return this.cnfTipoComprobanteService.getAllDataComboVentas().subscribe(data => {
      this.listCnfTipoComprobante = data;
      this.loadingCnfTipoComprobante = false;
    })

  }
  compareCnfTipoComprobante(a1: CnfTipoComprobante, a2: CnfTipoComprobante): boolean {
    if (a1 === undefined && a2 === undefined) {
      return true;
    }

    return (a1 === null || a2 === null || a1 === undefined || a2 === undefined)
      ? false : a1.id === a2.id;
  }
  getListInvAlmacen() {
    this.loadingInvAlmacen = true;
    console.log(this.chargingsb);
    return this.invAlmacenService.getAllByCnfLocalId(this.model.cnfLocal.id).subscribe(data => {
      this.listInvAlmacen = data;
      this.loadingInvAlmacen = false;
      if(this.listInvAlmacen.length == 1){
        this.invAlmacenService.getData(this.listInvAlmacen[0].id).subscribe(data => {
          this.model.invAlmacen = data;
        })
      }
      
    })

  }
  compareInvAlmacen(a1: InvAlmacen, a2: InvAlmacen): boolean {
    if (a1 === undefined && a2 === undefined) {
      return true;
    }

    return (a1 === null || a2 === null || a1 === undefined || a2 === undefined)
      ? false : a1.id === a2.id;
  }
  onChangeCantidad(item: any, value: any) {
    item.importe = value * item.precio;
    item.afectacionIgv
      = item.importe - item.importe / 1.18
    this.updateTotals()
  }
  onChangePrecio(item: any, value: any) {
    item.importe = item.cantidad * value;
    item.afectacionIgv
      = item.importe - item.importe / 1.18
    this.updateTotals()
  }
  updateTotals() {
    this.model.total = 0
    this.model.subtotal = 0
    this.model.igv = 0
    this.model.listActComprobanteDetalle.forEach(element => {
      this.model.total = Math.round((this.model.total + element.importe + Number.EPSILON) * 100) / 100;
      this.model.subtotal = Math.round((this.model.total / 1.18 + Number.EPSILON) * 100) / 100;
      this.model.descuento = 0
      this.model.igv = Math.round((this.model.total - this.model.subtotal + Number.EPSILON) * 100) / 100
    });
  }
  loadSerie() {
    this.model.serie = "";
    return this.cnfNumComprobanteService
      .getDataByCnfTipoComprobanteIdAndCnfLocalId(this.model.cnfTipoComprobante.id.toString()
        , this.model.cnfLocal.id.toString()).subscribe(data => {
          this.model.serie = data[0].serie
        })
  }
  ticketChild() {
    let myMap = new Map();
    myMap.set("id", this.model.id);
    myMap.set("tipo", 2);
    let mp = new UpdateParam();
    const convMapDetail: any = {};
    myMap.forEach((val: string, key: string) => {
      convMapDetail[key] = val;
    });
    console.log(convMapDetail);
    mp.map = convMapDetail;
    this.commonService.updateParam = mp;
    this.commonService.genericPostRequest("/api/business/getpdflocal", mp, 'blob').subscribe(data => {
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
    // console.log("enviando a imprimir: ",this.properties.listData);
  }
  a4() {
    let myMap = new Map();
    myMap.set("id", this.model.id);
    myMap.set("tipo", 1);
    let mp = new UpdateParam();
    const convMapDetail: any = {};
    myMap.forEach((val: string, key: string) => {
      convMapDetail[key] = val;
    });
    console.log(convMapDetail);

    mp.map = convMapDetail;
    this.commonService.genericPostRequest("/api/business/getpdflocal", mp, 'blob')
    .subscribe(data => {
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
  msgConfirmSaveWithButtons(title: string, icon: any, lines: any) {
    let html: string = "";
    console.log(lines);
    if (lines) {
      for (let items of lines) {
        html = html + '<div class="form-group row">' +

          '<div class="col-sm-12" style="padding-top: 20px;">' +
          '    <div class="row justify-content-center">';
        '<ng-container>'
        for (let index = 0; index < items.columns.length; index++) {
          const element = items.columns[index];
          console.log(element);

          html = html + '<div class="col-lg-6  col-sm-6 col-xs-5" style="margin-left: 50x;">' +
            '            <button  onclick="onBtnClicked()" id="' + element.id + '" class="btn btn-default col-12">' +
            '                <i class="' + element.icon + '"></i> ' + element.value +
            '            </button>' +
            '        </div>';
        }

        // '        <div class="col-lg-6  col-sm-6 col-xs-5" style="margin-left: 50x;">'+
        // '            <a id="ticket" onclick = '+items.action.name()+' class="btn btn-default col-12">'+
        // '                <i class="fa fa-arrow-circle-left"></i> Ticket'+
        // '            </a>'+
        // '        </div>'+
        // '        <div class="col-lg-6 col-sm-6 col-xs-5" style="margin-left: 20x;">'+
        // '            <a id="a4" class="btn btn-primary col-12">'+
        // '                <i class="fa fa-arrow-circle-left"></i> A4'+
        // '            </a>'+
        // '        </div>'+
        html = html + '    </div>' +
          '</div>' +
          '</div>';
      }
    }

    var onBtnClicked = () => {
      this.ticketChild();
    };
    return Swal.fire({
      title: title,
      icon: icon,
      html: '<div class="table-responsive">' +
        '<div class="col-sm-12" style="padding-top: 6px;">' +
        html +
        '</div>' +
        '</div>',
      showCloseButton: true,
      allowOutsideClick: false,
      showConfirmButton: true,

      confirmButtonText:
        '<div ><i class="fa fa-check" ></i> Aceptar</div>',


    })

  }
  generateAttachment(blob: Blob, extension: string) {
    const data = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = data;
    link.download = "report." + extension;
    link.dispatchEvent(new MouseEvent('click', {
      bubbles: true, cancelable: true, view: window
    }));
    setTimeout(() => {
      window.URL.revokeObjectURL(data);
      link.remove
    }, 100);
  }
  // onChangeinDetail(item:any,event: any){
  //   console.log(event);
  //   item.importe = item.cantidad * item.precio;
  //   this.model.total = Math.round((this.model.total + item.importe + Number.EPSILON) * 100) / 100 ;
  //   this.model.subtotal = Math.round((this.model.total / 1.18 + Number.EPSILON) * 100) / 100 ;
  //   this.model.descuento = 0
  //   this.model.igv = Math.round((this.model.total - this.model.subtotal  + Number.EPSILON) * 100) / 100
  // }
  removeItem(index:any){
    this.model.listActComprobanteDetalle.splice(index, 1)
    this.updateTotals()
  }
  getListCnfMestroAsObservable(term: any): Observable<any> {

    if (term.length >= 2) {
      let cnfEmpresa = this.appService.getProfile().profile.split("|")[1];
      return this.cnfMaestroService.getAllDataComboTypeHead(term,cnfEmpresa)
        .pipe(
          tap(() => this.searchFailed = false),
          catchError((err: any) => {
            console.log(err);
            this.searchFailed = true;
            return of([]);
          })
        );
    } else {
      return <any>[];
    }

  }
searchCnfMaestro = (text$: Observable<string>) =>
  text$.pipe(
    debounceTime(200),
    distinctUntilChanged(),
    switchMap(term => {
      return this.getListCnfMestroAsObservable(term);
    })
  )
}

