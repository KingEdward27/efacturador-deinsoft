import { AfterViewInit, Component, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { NgbActiveModal, NgbCalendar, NgbDateAdapter, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { HttpClient } from '@angular/common/http';
import { CommonService } from '../../services/common.service';
import { CustomAdapter, CustomDateParserFormatter } from '../../util/CustomDate';
import { UtilService } from '@services/util.service';
import { environment } from 'environments/environment';
import { UpdateParam } from '../model/UpdateParam';
import { GenericFormComponent } from '../generic-form/generic-form.component';



@Component({
  selector: 'app-generic-list3',
  templateUrl: './generic-list3.component.html',
  providers: [
    { provide: NgbDateAdapter, useClass: CustomAdapter },
    { provide: NgbDateParserFormatter, useClass: CustomDateParserFormatter }
  ]
})
export class GenericList3Component extends CommonService implements OnInit {

  //generic variables

  modelSearch: any;
  error: any;
  headers: any;
  dtOptions: any;
  listDetail: any;
  load: boolean = false;
  listDetail2: any[][] = [];
  dataTable!: DataTables.Api;
  widthIcons: any;
  datablesSettings: any
  @Input() props?: any;
  // @Output() result: EventEmitter<any> = new EventEmitter();
  // @Output() preSave2: EventEmitter<any> = new EventEmitter<any>();
  // @Output() beforeAdding: EventEmitter<any> = new EventEmitter();
  @Input() onPreLoadForm: () => boolean;
  @Input() onPreSave: () => boolean;
  @Input() newButton: () => boolean;
  @Input() editButton: () => boolean;
  @Input() route?: any;
  
  constructor(private utilService: UtilService, private httpClient: HttpClient, private router: Router, public commonService: CommonService) {
    super(httpClient);

  }
  ngOnInit(): void {
    this.properties = this.props;
    this.load = false;
    this.headers = this.properties.headers;
    this.commonService.baseEndpoint = environment.apiUrl;
    //aqui cargar metadata, nullable, tamaño, etc
    
    this.widthIcons = "25px";
    let lang = localStorage.getItem('lang');
    let listColumns = []
    let indexes = 0
    this.properties.columnsList.forEach(element => {
      listColumns.push(indexes)
      indexes++;
    });
    listColumns.push(this.properties.columnsList.length)
    // this.utilService.titleExport = this.properties.title
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
        title: this.properties.title,
        text: '<i class="fa fa-file-excel"></i>&nbsp; XLS',
        exportOptions: {
          columns: listColumns
        }
      },
      {
        extend: 'pdf',
        title: this.properties.title,
        text: ' <i class="fa fa-file-pdf"></i>&nbsp; PDF',
        orientation: 'landscape',
        exportOptions: {
          columns: listColumns
        }
      },
      {
        extend: 'csv',
        title: this.properties.title,
        text: '<i class="fa fa-file-excel"></i>&nbsp; CSV',
        exportOptions: {
          columns: listColumns
        }
      },

      ],
      language: {
        url: 'assets/i18n/datatables/lang' + lang?.toUpperCase() + '.json'
        // emptyTable: '',
        // zeroRecords: 'No hay coincidencias',
        // lengthMenu: 'Mostrar _MENU_ elementos',
        // search: 'Buscar:',
        // info: 'De _START_ a _END_ de _TOTAL_ elementos',
        // infoEmpty: 'De 0 a 0 de 0 elementos',
        // infoFiltered: '(filtrados de _MAX_ elementos totales)',
        // paginate: {
        //   first: 'Prim.',
        //   last: 'Últ.',
        //   next: 'Sig.',
        //   previous: 'Ant.'
        // }
      }
    }

    this.getListData(this.properties.columnsListParams);

    let com = this.commonService;
    this.properties.message?.rows?.forEach((element: any) => {
      element.columns.forEach((column: any) => {
        $(document).on('click', '#' + column.id, function () {
          //Some code 1
          column.action.name(column.param, com);

        });

      });

    });
  }
  actionByName(lineItem: any, func: any) {

    func.name(this.properties, lineItem, this.utilService);
  }
  addParams(params: any) {
    if (params) {
      let myMap = new Map();
      params.forEach((element2: any) => {
        const map = new Map<string, string>(Object.entries(element2));
        map.forEach((val: string, key: string) => {
          myMap.set(key, val);
        });
      });
      // let myMap = new Map();
      // params.forEach((element:any) => {
      //   myMap.set(element.tableName + '.' + element.columnName, inputFilter);
      // });
      const convMap: any = {};
      myMap.forEach((val: string, key: string) => {
        convMap[key] = val;
      });
      this.properties.filters = convMap;
    }
  }

  getListData(params: any) {
    this.addParams(params)
    this.baseEndpoint = environment.apiUrl;

    let myMap = new Map();
    this.properties.conditions?.forEach((element: any) => {
      myMap.set(element.columnName, element.value);
    })
    const convMap: any = {};
    myMap.forEach((val: string, key: string) => {
      convMap[key] = val;
    });
    this.properties.filters = convMap;

    super.getList(this.properties)
      .subscribe(data => {
        this.listDetail = data;
        setTimeout(() => {
          this.dataTable = $('#dtData' + this.properties.tableName).DataTable(
            this.datablesSettings);
        }, 1);
        this.dataTable?.destroy();
      });

  }
  exportData() {
    //console.log(this.headers);

    this.addParams(this.properties.columnsListParams);

    let headers: any[] = [];
    headers.push("Nro.");
    this.properties.columnsList.forEach((element: any) => {
      headers.push(this.utilService.getTranslate("webpagos." + element.tableName + "." + element.columnName))
    });
    this.properties.headers = headers;
    //console.log(this.properties.headers);

    super.export(this.properties).subscribe(data => {

      //console.log(data.body);
      if (data.body.type != 'application/json') {
        var contentType = 'application/pdf';
        var extension = "pdf";

        if (data.body.type == "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" ||
          data.body.type == "application/octet-stream") {
          contentType = data.body.type;
          extension = "xlsx";
        }
        const blob = new Blob([data.body], { type: contentType });
        this.generateAttachment(blob, extension);
      }
    });
  }
  async goToForm() {
    this.newButton()
    localStorage.setItem("propertiesId", JSON.stringify("0"));
    // this.properties.functions.forEach(element => {
    //   console.log(element.func);
    //   element.func();
    // });

    // const resultFromChild = this.onPreLoadForm.call(this, 20);
    // //console.log(resultFromChild);
    // if (resultFromChild) {
    //   this.properties.id = 0;
    //   this.properties.route = this.router.url;
    //   localStorage.setItem("properties", JSON.stringify(this.properties));
      
    //   //this.commonService.onPreSave = this.onPreSave;
    //   this.commonService.setPreSave(this.onPreSave);
    //   this.router.navigate(["/generic-form"]);
    // }


  }
  goToFormToEdit(id: any) {
    this.properties.id = id;
    localStorage.setItem("propertiesId", JSON.stringify(id));
    this.router.navigate(["/new-"+this.route.replace("/","")]);
    this.editButton()
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

    

  }
  delete(id: any) {
    this.utilService.confirmDelete(id).then((result) => {
      if (result) {
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
      if (element.filterType != 'none') {
        let inputFilter = (<HTMLInputElement>document.getElementById(element.tableName + '.' + element.columnName)).value;
        if (inputFilter) {
          myMap.set(element.tableName + '.' + element.columnName, inputFilter);
        }
      }

    });
    this.properties.conditions?.forEach((element: any) => {
      myMap.set(element.columnName, element.value);
    })
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

}

