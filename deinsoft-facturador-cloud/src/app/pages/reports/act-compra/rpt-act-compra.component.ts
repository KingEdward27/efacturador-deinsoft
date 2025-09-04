import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import Swal from 'sweetalert2';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Location } from '@angular/common';
import { NgbDateAdapter, NgbDateParserFormatter, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { GenericListComponent } from '@/base/components/generic-list/generic-list.component';
import { CustomAdapter, CustomDateParserFormatter } from '@/base/util/CustomDate';
import { UtilService } from '@services/util.service';
import { CommonService } from '@/base/services/common.service';
import { GenericMasterDetailFormComponent } from '@/base/components/generic-master-detail-form/generic-master-detail-form.component';
import { GenericList2Component } from '@/base/components/generic-list2/generic-list2.component';
import { AppService } from '@services/app.service';


@Component({
  selector: 'rpt-act-compra',
  templateUrl: '../../../base/components/generic-list2/generic-list2.component.html',
  providers: [
    { provide: NgbDateAdapter, useClass: CustomAdapter },
    { provide: NgbDateParserFormatter, useClass: CustomDateParserFormatter }
  ]

})
export class RptActCompraComponent extends GenericList2Component implements OnInit {
  ticket = "";
  prop = {
    "tableName": "act_comprobante",
    "title": "Reporte Compras",
    "columnsList": [{ tableName: "act_comprobante", columnName: "fecha", filterType: "text" },
                    { tableName: "cnf_local", columnName: "nombre", filterType: "text"  },
                    { tableName: "cnf_tipo_comprobante", columnName: "nombre", filterType: "none" },
                    { tableName: "act_comprobante", columnName: "serie", filterType: "text"},
                    { tableName: "act_comprobante", columnName: "numero", filterType: "text",type : "number" },
                    { tableName: "act_comprobante", columnName: "total", filterType: "text" ,type : "number"},
                    { tableName: "act_comprobante", columnName: "observacion", filterType: "text" },
                    { tableName: "cnf_maestro", columnName: "nombres", filterType: "text" },
                    { tableName: "cnf_forma_pago", columnName: "nombre", filterType: "none" },
                    { tableName: "cnf_moneda", columnName: "codigo", filterType: "none" },
                    { tableName: "inv_almacen", columnName: "nombre", filterType: "none" }
    ],
    columnsListParams:[],

    "foreignTables": [{ "tableName": "cnf_tipo_comprobante", "idValue": "cnf_tipo_comprobante_id" },
                      { "tableName": "cnf_maestro", "idValue": "cnf_maestro_id" },
                      { "tableName": "cnf_forma_pago", "idValue": "cnf_forma_pago_id" },
                      { "tableName": "cnf_moneda", "idValue": "cnf_moneda_id" },
                      { "tableName": "cnf_local", "idValue": "cnf_local_id" },
                      { "tableName": "inv_almacen", "idValue": "inv_almacen_id" }]
  }
  igv: number = 0;
  subtotal: number = 0;
  total: number = 0;
  constructor(private utilServices: UtilService, private httpClientChild: HttpClient, private routers: Router,
    private _location0: Location,public _commonService:CommonService,private appService:AppService) {
    super(utilServices, httpClientChild, routers,_commonService);
  }
  ngOnInit(): void {
    super.baseEndpoint = this.baseEndpoint;
    let cnfEmpresa = this.appService.getProfile().profile.split("|")[1];
    this.prop.columnsListParams.push({"columnName":"act_comprobante.flag_isventa","value":2});
    this.prop.columnsListParams.push({"columnName":"cnf_local.cnf_empresa_id","value":cnfEmpresa});
    super.properties = this.prop;

    localStorage.setItem("properties", JSON.stringify(this.properties));
    super.ngOnInit();
    
  }

  loadData() {

  }
  print(prope:any, param:CommonService,parent:GenericMasterDetailFormComponent) {
    console.log("imprimiendo desde cnf-org...", param);
    parent.properties = this.prop;
    
    // console.log("enviando a imprimir: ",this.properties.listData);
  }
  getProductDetail(properties: any, id: any) {
    let forSearch = {
      select: "cnf_producto.cnf_producto_id,cnf_producto.nombre,'','',cnf_producto.precio,''",
      from: "cnf_producto",
      where: { "cnf_producto_id": id }
    };
    properties.details[0].listData.onchange = forSearch;
    properties.details.forEach((element: any) => {

    });
  }
  searchProduct = function (param: any) {

  }
}
