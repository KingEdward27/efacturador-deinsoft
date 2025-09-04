import { GenericListComponent } from '@/base/components/generic-list/generic-list.component';
import { CommonService } from '@/base/services/common.service';
import { HttpClient } from '@angular/common/http';
import { Component,OnInit} from '@angular/core';
import { Router } from '@angular/router';
import { AppService } from '@services/app.service';
import { UtilService } from '@services/util.service';
@Component({
  selector: 'app-seg-usuario-empresa',
  templateUrl: '../../../base/components/generic-list/generic-list.component.html'
})

export class SegUsuarioEmpresaComponent extends GenericListComponent implements OnInit{
  //baseEndpoint = environment.apiUrl + '/get-all-cnf-org';
  prop ={
    "tableName": "seg_usuario",
    "title": "Usuarios",
    "api":"/api/framework/save-user",
    "columnsList":[{tableName: "seg_usuario", columnName:"nombre",filterType:"text"},
                   {tableName: "seg_usuario", columnName:"email",filterType:"text"}
                ],
    childTables:[
                  {tableName: "seg_rol",tableNameDetail: "seg_rol_usuario",
                    idValue:"seg_rol_id"
                    ,columnsForm: [
                                    {tableName:"seg_rol", columnName:"nombre",
                                    type:"select",loadState:1,relatedBy:"seg_rol_id"},
                                    {tableName:"cnf_local", columnName:"nombre",type:"select",loadState:1,
                                    relatedBy:"cnf_local_id",filters:[]}
                                  ]
                  }
    ],
    "columnsForm":[{tableName: "seg_usuario", "columnName":"nombre","type":"input"},
                   {tableName: "seg_usuario", columnName:"email",type:"input"},
                   {tableName: "seg_usuario", columnName:"password",type:"password"}
           ],
    "preSave" : [
            {columnForm:"estado",value:"1"}
          ],
    //filters sería para filtros adicionales
    "conditions":[],
    "orders":["nombre"]
  }
  constructor(private utilServices: UtilService,private httpClients:HttpClient,
    private routers: Router,public _commonService:CommonService,private appService:AppService) { 
    super(utilServices,httpClients,routers,_commonService);
  }
  ngOnInit(): void {
    super.baseEndpoint = this.baseEndpoint;
    let user = this.appService.getProfile();
    console.log(user);
    
    let cnfEmpresa = user.profile.split("|")[1];
    this.prop.conditions.push({"columnName":"seg_usuario.cnf_empresa_id","value":cnfEmpresa});
    this.prop.childTables[0].columnsForm[1].filters.push({"columnName":"cnf_local.cnf_empresa_id","value":cnfEmpresa});
    this.prop.preSave.push({columnForm:"cnf_empresa_id", "value":cnfEmpresa})
    super.properties = this.prop;
    console.log(this.prop);
    super.ngOnInit();
  }
  save(): void {
    
  }
}

