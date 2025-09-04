import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import Swal from 'sweetalert2';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Location } from '@angular/common';
import { NgbDateAdapter, NgbDateParserFormatter, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CustomAdapter, CustomDateParserFormatter } from '@/base/util/CustomDate';
import { GenericMasterDetailFormComponent } from '@/base/components/generic-master-detail-form/generic-master-detail-form.component';
import { UtilService } from '@services/util.service';
import { CommonService } from '@/base/services/common.service';


@Component({
  selector: 'act-compra-form',
  templateUrl: '../../base/components/generic-master-detail-form/generic-master-detail-form.component.html',
  providers: [
    { provide: NgbDateAdapter, useClass: CustomAdapter },
    { provide: NgbDateParserFormatter, useClass: CustomDateParserFormatter }
  ]

})
export class ActCompraFormComponent extends GenericMasterDetailFormComponent implements OnInit {
  ticket = "";
  prop = {
    "tableName": "act_comprobante",
    "title": "Compra",
    "api":"save-purchase",
    "columnsList": [{ tableName: "act_comprobante", columnName: "fecha", filterType: "text" },
                    { tableName: "cnf_local", columnName: "nombre", filterType: "text"  },
                    { tableName: "cnf_tipo_comprobante", columnName: "nombre", filterType: "none" },
                    { tableName: "act_comprobante", columnName: "numero", filterType: "text",disabled:"disabled" },
                    { tableName: "act_comprobante", columnName: "total", filterType: "text" },
                    { tableName: "act_comprobante", columnName: "observacion", filterType: "text" },
                    { tableName: "cnf_maestro", columnName: "nombres", filterType: "text" },
                    { tableName: "cnf_forma_pago", columnName: "nombre", filterType: "none" },
                    { tableName: "cnf_moneda", columnName: "codigo", filterType: "none" },
                    { tableName: "inv_almacen", columnName: "nombre", filterType: "none" }
    ],
    //"columnsList":["name","address","cnf_company.name","cnf_district.name"],
    "foreignTables": [{ "tableName": "cnf_tipo_comprobante", "idValue": "cnf_tipo_comprobante_id" },
                      { "tableName": "cnf_maestro", "idValue": "cnf_maestro_id" },
                      { "tableName": "cnf_forma_pago", "idValue": "cnf_forma_pago_id" },
                      { "tableName": "cnf_moneda", "idValue": "cnf_moneda_id" },
                      { "tableName": "cnf_local", "idValue": "cnf_local_id" },
                      { "tableName": "inv_almacen", "idValue": "inv_almacen_id" }],
    "columnsForm": [{ tableName: "cnf_local", "columnName": "nombre", "type": "select", loadState: 1, relatedBy: "cnf_local_id",loadFor:"inv_almacen_id",
                      load:{tableName:"inv_almacen",columnName:"nombre",loadBy:"cnf_local_id",id:"cnf_local_id"},value:1 },
                    { tableName: "act_comprobante", columnName: "fecha", type: "date" },
                    { tableName: "cnf_tipo_comprobante", "columnName": "nombre", "type": "select", loadState: 1, relatedBy: "cnf_tipo_comprobante_id" },
                    { tableName: "act_comprobante", "columnName": "serie", "type": "input" },
                    { tableName: "act_comprobante", columnName: "numero", type: "input" },
                    { tableName: "cnf_maestro", "columnName": "concat(nombres,' ',apellido_paterno,' ',apellido_materno)", "type": "select", loadState: 1, relatedBy: "cnf_maestro_id" },
                    { tableName: "cnf_forma_pago", "columnName": "nombre", "type": "select", loadState: 1, relatedBy: "cnf_forma_pago_id",value:1 },
                    { tableName: "act_comprobante", "columnName": "igv", "type": "label", value: "0.00", "subtype": "number",onchange:"total" },
                    { tableName: "act_comprobante", "columnName": "subtotal", "type": "label", value: "0.00", "subtype": "number" },
                    { tableName: "act_comprobante", "columnName": "total", "type": "label", value: "0.00", "subtype": "number" },
                    { tableName: "cnf_moneda", "columnName": "nombre", "type": "select", loadState: 1, relatedBy: "cnf_moneda_id" ,value:1},
                    { tableName: "act_comprobante", "columnName": "observacion", "type": "input-large" },
                    { tableName: "inv_almacen", "columnName":"nombre","type":"select",loadState : 0,loadFor:"cnf_local",relatedBy:"inv_almacen_id",value:1}

    ],
    "details": [
      {
        tableName: "act_comprobante_detalle", "relatedBy": "act_comprobante_id",
        search: {
          tableName: "cnf_producto", columnName: "cnf_producto.cnf_producto_id, cnf_producto.nombre",
           "type": "select", loadState: 1,where:"cnf_producto.nombre like concat('%',[value],'%')",
          relatedBy: "cnf_producto_id",
          onchange:{
              select: {
                  columns:"cnf_producto.cnf_producto_id,cnf_producto.nombre,'' tax,'' descripcion,'1' cantidad,cnf_producto.precio,cnf_producto.precio,'' actions",
                  from: "cnf_producto",
                  where:  "cnf_producto_id = get(0)" 
              }
          },
          "action": {}
        },
        columnsList: [
          { tableName: "cnf_producto", columnName: "cnf_producto_id", type: "hidden" },
          { tableName: "cnf_producto", columnName: "nombre", type: "none" },
          { tableName: "cnf_impuesto_condicion", "columnName": "nombre", "type": "select", loadState: 1, relatedBy: "cnf_impuesto_condicion_id" },
          { tableName: "act_comprobante_detalle", columnName: "descripcion", type: "input" },
          { tableName: "act_comprobante_detalle", columnName: "cantidad", type: "input", subtype: "number" },
          { tableName: "act_comprobante_detalle", columnName: "precio", type: "input", subtype: "number" },
          { tableName: "act_comprobante_detalle", columnName: "afectacion_igv", type: "input", disabled:"disabled"}
        ],
        postSaveTrans:[
                      // {type: "insert",
                      //   tableName: "inv_movimiento_producto",
                      //   values: [{"inv_movimiento_producto_id":"null"},
                      //           {"cantidad":"-1 * act_comprobante_detalle.cantidad"},
                      //           {"cnf_producto_id":"act_comprobante_detalle.cnf_producto_id"},
                      //           {"inv_almacen_id":"act_comprobante.inv_almacen_id"}]},
                      {type: "update",
                       tableName: "inv_movimiento_producto",
                       columns: "cantidad = cantidad - act_comprobante_detalle.cantidad",
                       where : [{"cnf_producto_id":"act_comprobante_detalle.cnf_producto_id"},
                                {"inv_almacen_id":"act_comprobante.inv_almacen_id"}]
                      }
                      ],
        "actions": []
      }
    ],
    //filters sería para filtros adicionales
    "conditions": { "act_comprobante.nombre": "", "act_comprobante.direccion": "" },
    "orders": ["nombre", "direccion"],
    "preSave" : [
      
                {columnForm:"flag_estado",value:"1"},
                {columnForm:"flag_isventa",value:"0"},
                {columnForm:"cnf_empresa_id",value:"1"}
              ],
    "postSave" : {message:{
                    title : {message :"Documento generado correctamente"},
                    icon :"success"
                    }
                  },
    "actions": [],

  }
  igv: number = 0;
  subtotal: number = 0;
  total: number = 0;
  constructor(private utilServices: UtilService, private httpClientChild: HttpClient, private routers: Router,
    private _location0: Location,public _commonService:CommonService) {
    super(utilServices, httpClientChild, routers, _location0,_commonService);
  }
  ngOnInit(): void {
    super.baseEndpoint = this.baseEndpoint;
    super.properties = this.prop;

    this.properties.details[0].search.postFunction = { name: this.updateTotals, param: 0, icon: "fas fa-print2" }
    this.properties.details[0].columnsList[5].onChange = { name: this.updateTotals, param: 0, icon: "fas fa-print2" }
    this.properties.details[0].columnsList[4].onChange = { name: this.updateTotals, param: 0, icon: "fas fa-print2" }
    this.properties.details[0].actions.push({ name: this.removeItems, icon: "fas fa-trash" })
    this.properties.urlReturn = "/compra"
    // this.properties.actions.push({ name: this.print, param: 0, icon: "fas fa-print" })
    localStorage.setItem("properties", JSON.stringify(this.properties));
    super.ngOnInit();
    console.log(this._commonService);
    
    // this.updateValue();
  }

  loadData() {

  }
  // preSave(){

  // }
  public async save(){
    let res = await super.preSave();
    console.log(res);
    if(res != 0){
      let result = await super.save();
      console.log(result);
      if(result){
        let res :any;
        res = await super.postSave();
        console.log(res);
        
        this.properties = this.prop;
      }
    }
    
    
  }
  print(prope:any, param:CommonService,parent:GenericMasterDetailFormComponent) {
    console.log("imprimiendo desde cnf-org...", param);
    parent.properties = this.prop;
    
    // console.log("enviando a imprimir: ",this.properties.listData);
  }
  updateTotals = function (properties: any) {
    let total = 0;
    properties.details[0].listData.forEach((element:any) => {
      let importe = Number(element[4]) * Number(element[5]);
      total = total + Math.round((importe + Number.EPSILON) * 100) / 100;
      element[6] = Math.round((importe - (importe / 1.18)+ Number.EPSILON) * 100) / 100;
    });
    let subtotal = Math.round(((total / 1.18) + Number.EPSILON) * 100) / 100;
    let igv = Math.round((total - subtotal + Number.EPSILON) * 100) / 100;
    properties.columnsForm.forEach((element: any) => {
      if (element.columnName == "igv") {
        element.value = igv;
      }
      if (element.columnName == "subtotal") {
        element.value = subtotal;
      }
      if (element.columnName == "total") {
        element.value = total;
      }
    });
    // console.log("enviando a imprimir: ",this.properties.listData);
  }
  removeItems(properties: any, index: any) {
    // console.log(properties.details[0].listData);

    // properties.columnsForm.forEach((element: any) => {
    //   if (element.columnName == "total") {
    //     element.value = Number(element.value) - Number(properties.details[0].listData[index][5] ?
    //       properties.details[0].listData[index][5] : "0");
    //   }
    // });
    console.log(properties);
    
    properties.details[0].listData.splice(index, 1);
    let total = 0;
    properties.details[0].listData.forEach((element:any) => {
      total = total + Math.round((Number(element[4]) * Number(element[5]) + Number.EPSILON) * 100) / 100;
    });
    let subtotal = Math.round(((total / 1.18) + Number.EPSILON) * 100) / 100;
    let igv = Math.round((total - subtotal + Number.EPSILON) * 100) / 100;
    properties.columnsForm.forEach((element: any) => {
      if (element.columnName == "igv") {
        element.value = igv;
      }
      if (element.columnName == "subtotal") {
        element.value = subtotal;
      }
      if (element.columnName == "total") {
        element.value = total;
      }
    });
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
