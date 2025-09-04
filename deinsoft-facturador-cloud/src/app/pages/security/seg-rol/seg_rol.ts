import { GenericListComponent } from '@/base/components/generic-list/generic-list.component';
import { CommonService } from '@/base/services/common.service';
import { HttpClient } from '@angular/common/http';
import { Component,OnInit} from '@angular/core';
import { Router } from '@angular/router';
import { UtilService } from '@services/util.service';
@Component({
  selector: 'app-seg-rol',
  templateUrl: '../../../base/components/generic-list/generic-list.component.html'
})

export class SegRolComponent extends GenericListComponent implements OnInit{
  //baseEndpoint = environment.apiUrl + '/get-all-cnf-org';
  prop ={
    "tableName": "seg_rol",
    "title": "Perfiles",
    "columnsList":[{tableName: "seg_rol", columnName:"nombre",filterType:"text"},
                   {tableName: "seg_rol", columnName:"descripcion",filterType:"text"}
                  ],
    "columnsForm":[{tableName: "seg_rol", "columnName":"nombre","type":"input"},
                   {tableName: "seg_rol", columnName:"descripcion",type:"input"}
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

