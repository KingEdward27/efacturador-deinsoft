import { AfterViewInit, Component, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { NgbActiveModal, NgbCalendar, NgbDateAdapter, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { HttpClient } from '@angular/common/http';
import { CommonService } from '../../services/common.service';
import { CustomAdapter, CustomDateParserFormatter } from '../../util/CustomDate';
import { UtilService } from '@services/util.service';
import { environment } from 'environments/environment';
import { UpdateParam } from '../model/UpdateParam';



@Component({
  selector: 'app-generic-list2',
  templateUrl: './generic-list2.component.html',
  providers: [
    { provide: NgbDateAdapter, useClass: CustomAdapter },
    { provide: NgbDateParserFormatter, useClass: CustomDateParserFormatter }
  ]
})
export class GenericList2Component extends CommonService implements OnInit {

  //generic variables

  modelSearch: any;
  error: any;
  headers: any;
  dtOptions:any;
  listDetail: any;
  load: boolean = false;
  listDetail2: any[][] = [];
  dataTable!: DataTables.Api;
  widthIcons:any;
  datablesSettings : any
  @Output() result: EventEmitter<any> = new EventEmitter();
  constructor(private utilService: UtilService, private httpClient: HttpClient, private router: Router,public commonService:CommonService) {
    super(httpClient);
  }
  ngOnInit(): void {
    this.load = false;
    this.headers = this.properties.headers;
    this.commonService.baseEndpoint = environment.apiUrl;
    //aqui cargar metadata, nullable, tamaño, etc
    this.properties.columnsForm?.forEach((element: any) => {
      element.isNull = false;
    });
    this.properties.columnsList?.forEach((element: any) => {
      if(!element.alias){
        element.alias = 'webpagos.'+element.tableName+"."+element.columnName
      }else{
        element.alias = 'webpagos.'+element.alias
      }
    });
    this.widthIcons = "25px";
    let lang = localStorage.getItem('lang');
    console.log(lang);
    this.datablesSettings = {
      deferRender: true,
      deferLoading: 7,
      pagingType: 'full_numbers',
      searching: false,
      processing: true,
      lengthMenu: [25, 50, 100],
      order: [[0, "asc"]],
      dom: 'lBftip',
      buttons: [{
        extend: 'excel',
        title : this.properties.title,
        text: '<i class="fa fa-file-excel"></i>&nbsp; XLS',
        exportOptions: {
          columns: ':visible'
        }
      },
      {
        extend: 'pdf',
        title : this.properties.title,
        text: ' <i class="fa fa-file-pdf"></i>&nbsp; PDF',
        orientation: 'landscape'
      },
      {
        extend: 'csv',
        title : this.properties.title,
        text: '<i class="fa fa-file-excel"></i>&nbsp; CSV',
        exportOptions: {
          columns: ':visible'
        }
      },

      ],
      language: {
        url: 'assets/i18n/datatables/lang' + lang?.toUpperCase() + '.json'
      }
    }

    this.getListData(this.properties.columnsListParams);
    
    let com = this.commonService;
    this.properties.message?.rows?.forEach((element:any) => {
      console.log(element);
      element.columns.forEach((column:any) => {
          $(document).on('click', '#'+column.id, function() {
            //Some code 1
            console.log( "adsd");
            column.action.name(column.param,com);
            
          });
        
      });
      
    }); 
  }
  actionByName(lineItem:any, func:any){
    console.log(lineItem,func,this.utilService);
    
    func.name(this.properties,lineItem,this.utilService);
  }
  addParams(params:any){
    console.log(params);
    
    if(params){
      let myMap = new Map();
      params.forEach((element: any) => {
        myMap.set(element.columnName, element.value);
      })
      // let myMap = new Map();
      // params.forEach((element2:any) => {
      //   const map = new Map<string,string>(Object.entries(element2));
      //   map.forEach((val: string, key: string) => {
      //     myMap.set(key, val);
      //   });
      // });
      // let myMap = new Map();
      // params.forEach((element:any) => {
      //   myMap.set(element.tableName + '.' + element.columnName, inputFilter);
      // });
      const convMap: any = {};
      myMap.forEach((val: string, key: string) => {
        convMap[key] = val;
      });
      console.log(convMap);
      this.properties.filters = convMap;
    }
  }
  
  getListData(params:any) {
    // if(params){
    //   let myMap = new Map();
    //   params.forEach((element2:any) => {
    //     const map = new Map<string,string>(Object.entries(element2));
    //     map.forEach((val: string, key: string) => {
    //       myMap.set(key, val);
    //     });
    //   });
    //   const convMap: any = {};
    //   myMap.forEach((val: string, key: string) => {
    //     convMap[key] = val;
    //   });
    //   console.log(convMap);
    //   this.properties.filters = convMap;
    // }
    this.addParams(params)
    this.baseEndpoint = environment.apiUrl;

    console.log(this.properties);
    super.getList(this.properties)
      .subscribe(data => {
        this.listDetail = data;
        console.log(this.headers);
        console.log(this.listDetail);
        setTimeout(() => {
          this.dataTable = $('#dtData'+this.properties.tableName).DataTable(
            this.datablesSettings);
        }, 1);
        this.dataTable?.destroy();
        // console.log(data);
      });
    
  }
  exportData(){
    console.log(this.headers);

    this.addParams(this.properties.columnsListParams);

    let headers :any[] = [];
    headers.push("Nro.");
    this.properties.columnsList.forEach((element:any) => {
      headers.push(this.utilService.getTranslate("webpagos."+element.tableName + "." + element.columnName))
    });
    this.properties.headers = headers;
    console.log(this.properties.headers);
    
    super.export(this.properties).subscribe(data => {
        
        console.log(data.body);
        if(data.body.type != 'application/json'){
          var contentType ='application/pdf';
          var extension = "pdf";
  
          if(data.body.type == "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" ||
          data.body.type == "application/octet-stream"){
            contentType = data.body.type;
            extension = "xlsx";
          }
          const blob = new Blob([data.body],{type: contentType});
          this.generateAttachment(blob,extension);
        }
      });
  }
  goToForm() {
    this.properties.id = 0;
    this.properties.route = this.router.url;
    localStorage.setItem("properties", JSON.stringify(this.properties));
    this.router.navigate(["/generic-form"]);
  }
  goToFormToEdit(id: any) {
    this.properties.id = id;
    
    // console.log(this.properties);
    // let listColumns:any[] = [];
    // this.properties.columnsForm.forEach((element:any) => {
      
    //   if(this.properties.tableName == element.tableName){
    //     listColumns.push(element.columnName);
    //   }
    //   if(element.relatedBy){
    //     listColumns.push(element.relatedBy);
    //   }
      
    // });
    // console.log(listColumns);
    
    super.getData(this.properties).subscribe(data => {
      console.log(data);
      let index = 0;
      // data.prototype.forEach((value: boolean, key: string) => {
      //     console.log(key, value);
      // });
      // var resultArray = Object.keys({}).map(function(personNamedIndex:any){
      //     let person = data[personNamedIndex];
      //     // do something with person
      //     console.log(person);
          
      // });
      let objects = [];
      for (let key in data) {
        // const convMap: any = {};
        // let key2 = key.replace("__",".");
        // convMap[key2] = data[key];
        // objects.push(convMap);
        const convMap: any = [];
        let key2 = key.replace("__",".");
        convMap[0] = key2;
        convMap[1] = data[key];
        objects.push(convMap);
      }
      console.log(objects);
      for (const element of objects) {
        let tablaAndColumndArr = element[0].split(".");
        for (const element2 of this.properties.columnsForm) {
          let tableAndColumn = element2.tableName + "." +element2.columnName;
          if((element[0] == tableAndColumn && (element2.type == 'input' || element2.type == 'date'))){
            element2.value = element[1];
            console.log(element[1]);
            break;
          }
          if(tablaAndColumndArr[0] == element2.tableName && element2.type == 'select' &&  
          (element2.relatedBy == tablaAndColumndArr[1] || element2.load?.loadBy == tablaAndColumndArr[1])){
            element2.value = element[1];
            break;
          }
          // let tablaAndColumnd = element[0].split(".");
          // // console.log(tablaAndColumnd);
          // if(tablaAndColumnd[0] == element2.tableName && element2.columnName != tablaAndColumnd[1]){
          //   element2.value = element[1];
          //   break;
          // }
        }
      }
      // objects.forEach(element => {
      //   this.properties.columnsForm.forEach((c:any) => {
      //     if(element[0] == element2.tableName + "." +element2.columnName){
      //       element2.value = element[1];
      //     }
      //     let tablaAndColumnd = element[0].split(".");

      //     if(tablaAndColumnd[0] == element2.tableName && element2.columnName != tablaAndColumnd[1]){
      //       element2.value = element[1];
      //     }
      //     // if(this.properties.tableName == element.tableName){
      //     //   index++;
      //     //   this.properties.columnsForm[index].value = data[index];
      //     // }
      //     // if(element.relatedBy){
      //     //   index++;
      //     //   this.properties.columnsForm[index].value = data[index];
      //     // }
          
      //   });
      // });
      this.properties.route = this.router.url;
      localStorage.setItem("properties", JSON.stringify(this.properties));
      // for (let index = 0; index < this.properties.columnsForm.length; index++) {
      //   if(this.properties.tableName == this.properties.columnsForm[index].tableName){
      //     this.properties.columnsForm[index].value = data[index];
      //   }
      // }
      // for (let index = 0; index < data.length; index++) {
      //   const element = data[index];
      //   this.properties.columnsForm[index].value = element;
      //   // for (let index2 = 0; index2 < this.properties.columnsForm.length; index2++) {
      //   //   const element = this.properties.columnsForm[index2];
      //   //   if(index == index2){
      //   //     this.properties.columnsForm[index2].value = element;
      //   //   }

      //   // }
      // }
      console.log(this.properties);
      console.log(this.router.url);
      
      
      this.router.navigate(["/generic-form"]);
    });
    
  }
  delete(id: any) {
    this.utilService.confirmDelete(id).then((result) => { 
      if(result){
        this.remove(this.properties.tableName, id).subscribe(() => {
          this.utilService.msgOkDelete();
          this.getListData(this.properties.columnsList.params);
        });
      }
      
    });
  }
  getListWithParams() {
    // let filters:any[] = [];
    let myMap = new Map();
    this.properties.columnsList.forEach((element: any) => {
      console.log(element)
      if (element.filterType != 'none') {
        let inputFilter = (<HTMLInputElement>document.getElementById(element.tableName + '.' + element.columnName)).value;
        if (inputFilter) {
          myMap.set(element.tableName + '.' + element.columnName, inputFilter);
        }
      }

    });
    const convMap: any = {};
    myMap.forEach((val: string, key: string) => {
      convMap[key] = val;
    });
    console.log(convMap);
    this.properties.filters = convMap;
    console.log(this.properties);

    super.getListWithFilters(this.properties).subscribe(data => {
      this.listDetail = data;
      console.log(this.listDetail);
      setTimeout(() => {
        this.dataTable = $('#dtData').DataTable(this.datablesSettings);
      }, 1);
      //this.dataTable?.destroy();
      // console.log(data);
    });
    
  }
  generateAttachment(blob:Blob,extension:string){
    const data = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = data;
      link.download = "report."+extension;
      link.dispatchEvent(new MouseEvent('click',{
        bubbles:true,cancelable:true,view:window 
      }));
      setTimeout(() => {
        window.URL.revokeObjectURL(data);
        link.remove
      }, 100);
  }
  
}

