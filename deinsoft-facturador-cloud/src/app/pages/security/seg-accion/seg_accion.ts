import { GenericListComponent } from '@/base/components/generic-list/generic-list.component';
import { CommonService } from '@/base/services/common.service';
import { HttpClient } from '@angular/common/http';
import { Component,OnInit} from '@angular/core';
import { Router } from '@angular/router';
import { UtilService } from '@services/util.service';
@Component({
  selector: 'app-seg-accion',
  templateUrl: '../../../base/components/generic-list/generic-list.component.html'
})

export class SegAccionComponent extends GenericListComponent implements OnInit{
  //baseEndpoint = environment.apiUrl + '/get-all-cnf-org';
  prop ={
    "tableName": "seg_accion",
    "title": "Perfiles",
    "columnsList":[{tableName: "seg_accion", columnName:"nombre",filterType:"text"},
                   {tableName: "seg_accion", columnName:"descripcion",filterType:"text"}
                  ],
    "columnsForm":[{tableName: "seg_accion", "columnName":"nombre","type":"input"},
                   {tableName: "seg_accion", columnName:"descripcion",type:"input"}
                  ],
    //filters sería para filtros adicionales
    "conditions":[],
    "orders":["nombre"]
  }
  constructor(private utilServices: UtilService,private httpClients:HttpClient,private routers: Router,public _commonService:CommonService) { 
    super(utilServices,httpClients,routers,_commonService);
  }
  ngOnInit(): void {
    super.baseEndpoint = this.baseEndpoint;
    super.properties = this.prop;
    console.log(this.prop);
    super.ngOnInit();
  }
  save(): void {
    
  }
}

