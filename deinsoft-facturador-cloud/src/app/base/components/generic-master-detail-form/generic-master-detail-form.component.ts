import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Observable, of, Subscription } from "rxjs";
import { CommonService } from '../../services/common.service';
import { NgbCalendar, NgbDateAdapter, NgbDateParserFormatter, NgbDateStruct, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CustomAdapter, CustomDateParserFormatter } from '../../util/CustomDate';
import { Action } from 'rxjs/internal/scheduler/Action';
import * as dayjs from 'dayjs';
import { catchError, debounceTime, distinctUntilChanged, switchMap, tap } from 'rxjs/operators';
import { GenericModalComponent } from '../generic-modal/generic-modal.component';
import Swal from 'sweetalert2';
import { BaseService } from '../../services/base.service';
import { UpdateParam } from '../model/UpdateParam';
import { data } from 'jquery';
import { UtilService } from '@services/util.service';
import { environment } from 'environments/environment';

@Component({
  selector: 'app-generic-master-detail-form',
  templateUrl: './generic-master-detail-form.component.html',

  providers: [
    { provide: NgbDateAdapter, useClass: CustomAdapter },
    { provide: NgbDateParserFormatter, useClass: CustomDateParserFormatter }
  ]
})
export class GenericMasterDetailFormComponent extends BaseService  implements OnInit {

  //generic variables
  error: any;
  errorGen: any;
  isDataLoaded: Boolean = false;
  isOk: boolean = false;
  isWarning: boolean = false;
  isDanger: boolean = false;
  message: any = "";
  listDetail: any;
  //variables propias
  id: any;
  subtotal:number = 0;
  igv:number = 0;
  total:number = 0;
  searchFailed = false;
  product:any;
  formatter = (x: string ) => x;
  public modalRef!: NgbModalRef;
  modalsNumber: any;
  saved:any = "0";
  // listLists: any[] = [];
  constructor(private utilService: UtilService, private httpClient: HttpClient,
    public router: Router, private _location: Location,public commonService:CommonService) {
      super();
      // this.modalService.activeInstances.subscribe((list:any) => {
      //   this.modalsNumber = list.length;
      // });
    
  }
  getService(){
    return this.commonService;
  }
  ngOnInit(): void {
    this.isDataLoaded = false;
    this.commonService.baseEndpoint = environment.apiUrl;
    console.log(this.properties);
    this.loadForm();
    console.log(this.properties);
    this.properties.columnsForm.forEach((element:any) => {
      if(element.type == 'date'){
        element.value = this.format(this.utilService.ngbCalendar.getToday()!);
      }
      
    });
    this.properties.details.forEach((element:any) => {
      element.functionSearch = (text$: Observable<string>) =>
      text$.pipe(
        debounceTime(200),
        distinctUntilChanged(),
        switchMap(term => {
          console.log("esg");
          console.log(term);
          
          return this.getListInDetailsAsObservable(term,element);
        })
      )
    });
    this.properties.columnsForm.forEach((element:any) => {
      element.functionSearch = (text$: Observable<string>) =>
      text$.pipe(
        debounceTime(200),
        distinctUntilChanged(),
        switchMap(term => {
          console.log("esg");
          console.log(term);
          
          return this.getListAsObservable(term,element);
        })
      )
    });
    console.log(this.commonService);
    let com = this.commonService;
    let wa = this.properties;
    let that = this;
    // this.utilService.msgConfirmSaveWithButtons(
    //   this.properties.postSave.message.title.message,
    //   this.properties.postSave.message.icon,
    //   this.properties.postSave.message.rows).then((result) => {
    //     console.log("Waaa");
    //     this.router.navigate(["/act-venta"]);
    //   });
    //this.viewDetail(null);
    this.properties.postSave.message.rows.forEach((element:any) => {
      console.log(element);
      element.columns.forEach((column:any) => {
        console.log(column.exists);
        
        // if(!column.exists){
        //   console.log("wd?");
        //   column.exists = "1";
        // if (element?.getAttribute('listener') !== 'true') {
        //     element?.addEventListener('click', function (e) {
        //         const elementClicked = e.target;
        //         elementClicked?.setAttribute('listener', 'true');
        //         console.log('event has been attached');
        //   });
        // }
        
        // if (-1 !== $.inArray(column.action.name(wa,com,that), button.data('events').click)) {
        //     //button.click(column.action.name(wa,com,that));
        //     $(document).on('click', '#'+column.id, function() {
        //       //Some code 1
        //       console.log( );
              
        //       column.action.name(wa,com,that);
              
        //       //Swal.clickConfirm();
        //     });
        // }
        //$('#'+column.id+':not(.bound)').addClass('bound').on('click',  column.action.name(wa,com,that));
        this.saved = JSON.parse(localStorage.getItem("saved") || '0');
        console.log(this.saved);
        
        //$('#'+column.id).off('click');
          $(document).on('click', '#'+column.id, function() {
            //Some code 1
            console.log( );
            let ticketOperacion = JSON.parse(localStorage.getItem("ticketOperacion") || '0');
            if(ticketOperacion != that.properties.ticketOperacion) return;
            column.action.name(wa,com,that);
            
            //Swal.clickConfirm();
          });
        // }
        //}
        
        
      });
      
    });  
    
    // $(document).on('click', '#a4', function() {
        //Some code 1
        // console.log('A4');
        //Swal.clickConfirm();
    // }); 
  }
  // ticket(){
  //   let myMap = new Map();
  //   myMap.set("ticketOperacion", 1);
  //   myMap.set("tipo",1);
  //   let mp = new UpdateParam();
  //   mp.map = myMap;
  //   this.commonService.genericRequest("/business/getpdf",mp).subscribe(data => {
  //     console.log(data);
  //   });
  // }
  mapToObjectMap(map: any) {
    const convMap: any = {};
    map.forEach((val: string, key: string) => {
      convMap[key] = val;
    });
    return convMap;
  }
  load(columnForm: any, index: number,event:any) {
    // console.log("waaa");
    // console.log(event);
      
      if(event){
        this.properties.columnsForm.forEach((element: any) => {
          if(element.tableName+"."+element.relatedBy == columnForm.tableName+"."+columnForm.relatedBy){
            element.text = event.target.options[event.target.options.selectedIndex].text;
          }
        });
        
      }
    //evento onchange, carga otros items
    if (columnForm.load) {
      console.log(this.properties);
      let conditions = columnForm?.load.loadBy.split(",")
      let myMap = new Map();
      conditions.forEach((cond:any) => {
        let column = columnForm?.tableName + "." + columnForm?.load.id;
        console.log(column);
        
        let selectValue = (<HTMLInputElement>document.getElementById(column)).value;
        //columnForm.value = selectValue
        
        if(selectValue){
          //myMap.set(cond,columnForm.value?columnForm.value:selectValue);
          myMap.set(cond,selectValue);
        }
        
      });
      
      const convMap = this.mapToObjectMap(myMap);


      // this.properties.columnsForm.forEach((element2:any) => {
      //   for (let index = 0; index < this.listLists.length; index++) {
      //     const element = this.listLists[index];
      //     element.forEach((item: any) => {
      //       if (columnForm.load.tableName == item[2]) {
      //         if(columnForm.loadState == 0 && columnForm.loadFor == element2.loadFor){
      //           this.listLists[index] = [];
      //           this.listLists[index].push([0, "- Seleccione -", element2.tableName]);
      //         }
      //       }
      //     });


      //   }

      // });

      let prop = { "tableName": columnForm.load.tableName, "columnsListWithOutProp": columnForm.load.columnName, "filters": convMap };
      console.log(prop);

      this.commonService.getListComboWithFilters(prop).subscribe(data2 => {
        console.log(data2);
        
          if(columnForm.load.getValue){
            console.log(data2.length);
            
            if(data2.length > 0){
              this.properties.columnsForm.forEach((element: any) => {
                if (columnForm.loadFor == element.columnName) {
                  element.value = data2[0][Number(columnForm.load.getValue)];
                }
              });
            }else{
              console.log("xd");
              
              this.properties.columnsForm.forEach((element: any) => {
                if (columnForm.loadFor == element.columnName) {
                  element.value = "";
                }
              });
            }
            
            
          }else{
            data2.push([0, "- Seleccione -", columnForm.load.tableName]);
            data2.sort();
            this.properties.columnsForm.forEach((element: any) => {
              if (columnForm.load.tableName == element.tableName) {
                element.value = element.value?element.value:0;
                element.listData = data2
              }
            });
          }
      });
    }
    console.log(this.properties);
  }
  onchange(postFunction:any, item:any,event:any,input:any){
    console.log(event);
    console.log(this.product);
    
    let condition:any = "";
    //if(!event.target.options[event.target.options.selectedIndex].text.includes("eccione")){
      console.log(event.item);
      if(item.search.onchange.select.where.includes("get(")){
        //item.onchange.select.where.replace("get(","").replace(")","");
        let column = item.search?.tableName + "." + item.search?.relatedBy;
        let t = (<HTMLInputElement>document.getElementById(column))
        console.log(column);
        
        // var selectValue = t.value;
        
        let selectValue2 = event.item;
        console.log(selectValue2);
        condition = item.search.onchange.select.where.replace("get(0)","'"+selectValue2[0]+"'");
      }
      console.log(condition);
      this.commonService.selectByTableNameAndColumns(item.search.onchange.select.from, 
        item.search.onchange.select.columns,
        condition).subscribe(data => {
        console.log(data);
        item.listData.push(data[0]);
        input.value = "";
        // this.properties.columnsForm.forEach((element: any) => {
        //   if (element.columnName == "total") {
        //     element.value = Number(element.value) + Number(data[0][5]);
        //   }
        //   if (element.columnName == "igv") {
        //     element.value = Number(element.value) + Number(data[0][5]);
        //   }
        //   if (element.columnName == "subtotal") {
        //     element.value = Number(element.value) + Number(data[0][5]);
        //   }
        // });
        // item.search.onchange.update.forEach((element:any) => {
        //   let actions = element.split(",");
        //   actions.forEach((action:any) => {
        //     console.log(action);
        //   });
        // });
        postFunction.name(this.properties);
      });
    //}
    
  }
  actionMessage(paramAction:any){
    console.log("waas");
    let com = this.commonService;
    let wa = this.properties;
    let that = this;
    paramAction.name();
  }
  action2(paramAction:any,index:any){
    paramAction.name(this.properties, index);
  }
  actions(paramAction:any,detailLine:any){
    console.log(this.properties);
    
    console.log(paramAction);
    let column = detailLine?.search.tableName + "." + detailLine?.search.relatedBy;
    let selectValue = (<HTMLInputElement>document.getElementById(column)).value;
    paramAction.name(this.properties,selectValue);
    // console.log();
    // detailLine.listData.push(result.list);
    
    // this.properties.columnsForm.forEach((element:any) => {
    //   result.labels.forEach((element2:any) => {
    //     if(element.tableName + "."+element.columnName == element2.name){
    //       element.value = Number(element.value) + Number(element2.value);
    //     }
    //   });
    // });
    // this.properties.details.forEach((element:any) => {
      
    //   if(element.tableName == detailLine.tableName){
    //     console.log(detailLine.columnList);
    //     detailLine.columnList.forEach((column:any) => {
          
    //     });
    //     let dataVacia: any[] = [];
    //     //dataVacia.push([0, "Producto prueba", "IGV","Producto prueba","","1","3.50",""]);
    //     detailLine.listData.push([1, "Producto prueba", "","Producto prueba","1","3.50"]);
    //   }
    // });
    console.log(this.properties);
    
  }
  removeLine(index1:any,index2:number){
    console.log(index1,index2);
    
    // let index = 0;
    // this.properties.details.forEach((element:any) => {
    //   index++;
    //   if(index == index1){
    //     element.listData.splice(index2,1);
    //   }
    // });
    index1.splice(index2,1);
  }
  fix(obj:any){

    for (var property in obj) {
        if (obj.hasOwnProperty(property)) {
            obj[property] = eval("(" + obj[property] + ")");
        }
    }

  }
  public updateValue(tableName:any,columnName:any,value:any){
    this.properties = JSON.parse(localStorage.getItem("properties") || '{}');
    //get functions
    // this.fix(this.properties);
    console.log(this.properties);
    
    this.properties.columnsForm.forEach((element:any) => {
      if(element.tableName == tableName && element.columnName == columnName){
        element.value = value;
      }
    });
    localStorage.setItem("properties", JSON.stringify(this.properties));
    console.log(this.properties);
  }
  // loadDetail(detail: any, index: number) {
  //   console.log(this.properties);
  //   console.log(detail);
  //   this.properties.details.forEach((element:any) => {
      
  //     if(element.tableName == detail.tableName){
  //       console.log(detail.columnList);
  //       detail.columnList.forEach((column:any) => {
          
  //       });
  //       let dataVacia: any[] = [];
  //       //dataVacia.push([0, "Producto prueba", "IGV","Producto prueba","","1","3.50",""]);
  //       detail.listData.push([1, "Producto prueba", "","Producto prueba","1","3.50"]);
  //     }
  //   });
    
  // }
  fromModel(value: string | null): NgbDateStruct | null {
    if (value) {
      let date = value.split("-");
      return {
        year : parseInt(date[0], 10),
        month : parseInt(date[1], 10),
        day : parseInt(date[2], 10)
      };
    }
    return null;
  }
  format(date: NgbDateStruct | null): string {
    return date ? this.leftPad(date.day.toString(),2,'0') + "/" + this.leftPad(date.month.toString(),2,'0') 
    + "/" + date.year : '';
  }
  leftPad(str: string, len: number, ch='.'): string {
    len = len - str.length + 1;
    return len > 0 ?
      new Array(len).join(ch) + str : str;
  }
  async loadForm() {
    // this.properties = JSON.parse(localStorage.getItem("properties") || '{}');
    for (let index = 0; index < this.properties.columnsForm.length; index++) {
      const element = this.properties.columnsForm[index];
      element.order = index + 1;
    }
    this.properties.details.forEach((element:any) => {
      element.listData = [];
    });
    await this.properties.columnsForm.forEach((element: any) => {
      if (element.type == 'date') {
        let wa = this.format(this.fromModel(element.value));
        console.log(wa);

        element.value = wa;
      }

      if (element.type != 'input' && element.type != 'date') {
        if (this.properties.id == 0) {
          if (element.loadState == 1) {
            this.commonService.getListComboByTableName(element.tableName, element.columnName,"").subscribe(data => {
              data.push([0, "- Seleccione -"]);
              data.sort();
              element.listData = data;
            });
          } else {
            let dataVacia: any[] = [];
            dataVacia.push([0, "- Seleccione -"]);
            element.listData = dataVacia;
          }
        } else {
          if (element.loadState == 1) {
            this.commonService.getListComboByTableName(element.tableName, element.columnName,"").subscribe(data => {
              data.push([0, "- Seleccione -"]);
              data.sort();
              element.listData = data;
              if(element.load){
                this.load(element,element.order,null)
              }
            });
          }

        }

      }
    });
    this.properties.columnsForm.forEach((element: any) => {
      if (element.type != 'input' && element.type != 'date') {
        if (this.properties.id != 0) {
          if (element.load) {
            console.log(element);
            // let column = element?.load.tableName + "." + element?.load.loadBy;
            let selectValue = element.value;
            let myMap = new Map();
            myMap.set(element?.load.loadBy, selectValue);
            const convMap = this.mapToObjectMap(myMap);
            let prop = { "tableName": element.load.tableName, "columnsListWithOutProp": element.load.columnName, "filters": convMap };
            console.log(prop);
            this.commonService.getListComboWithFilters(prop).subscribe(data2 => {
              // console.log(element.order+1);
              console.log(data2);

              // this.properties.columnsForm.forEach(element => {

              // });
              let nextElement = this.properties.columnsForm[element.order];
              console.log(nextElement)
              data2.push([0, "- Seleccione -"]);
              data2.sort();
              if (nextElement) {
                nextElement.listData = data2;
              }
              console.log(this.properties);
              // element.listData = data;
            });
          }
        }
      }

    });

   
    this.isDataLoaded = true;
    this.properties.details.forEach((element: any) => {
      if (element.search) {
        this.commonService.getListComboByTableName(element.search.tableName, element.search.columnName,"").subscribe(data => {
          console.log(data);
          data.push([0, "- Seleccione -"]);
          data.sort();
          element.search.listData = data;
        });
      }
      element.columnsList.forEach((element2:any) => {
        console.log(element2);
        
        if(element2.type == 'select'){
          this.commonService.getListComboByTableName(element2.tableName, element2.columnName,"").subscribe(data => {
            console.log(data);
            data.push([0, "- Seleccione -"]);
            data.sort();
            element2.listData = data;
          });
        }
      });
    });
    console.log(this.properties);
  }
  loadData() {
    if (this.properties.id) {
      //cargar datos
      //pintarlos en los inputs
    }
  }
  back() {
    this.router.navigate(["/"]);
  }
  async preSave(){
    this.errorGen = undefined;
    this.properties.details.forEach(element => {
      console.log(element);
      
      if(element.listData.length == 0){
        this.errorGen = "Debe agregar productos/servicios a la operación"
      }
    });
    console.log(this.errorGen);
    
    if(!this.errorGen){
      this.preSaveStatic();
      await this.preSaveDinamic();
      return 1
    }else{
      return 0
    }
    
  }
  preSaveStatic()
  {  
    console.log(this.properties.preSave);
    
    if(this.properties.preSave){
      this.properties.preSave.forEach((element:any) => {
        console.log(element);
        
          this.properties.columnsForm.push({tableName:this.properties.tableName, 
            columnName:element.columnForm,value:element.value,type :"hidden"})
      });
    }
  }
  async postSave(){
    console.log(this.properties);
    
    if(this.properties.postSave){
      let contador = -1;
      console.log(this.properties.postSave.message.title.message);
      
      this.properties.postSave.message.title.messageTemp = this.properties.postSave.message.title.message;
      console.log(this.properties);
      
      if(this.properties.postSave.message.title.params){
        this.properties.postSave.message.title.params.split(",").forEach((element:any) => {
        
          console.log(element.replace(/\s/g, '').split("->"));
          let column = element.replace(/\s/g, '').split("->")[1]
         
          let type ;
          this.properties.columnsForm.forEach((columnForm:any) => {
            
            
            if(columnForm.tableName+"."+columnForm.columnName == column){
              console.log(columnForm.tableName+"."+columnForm.columnName );
              column = columnForm;
              // if(columnForm.type == 'select'){
              //   column = columnForm.tableName+"."+columnForm.relatedBy;
              // }else{
              //   column = 
              // }
              type = columnForm.type;
            }
          });
          let val:any;
          contador++;
          if(type == 'select'){
            val = column.text;
          }else{
            //let selectValue = (<HTMLInputElement>document.getElementById(columnForm.tableName+"."+columnForm.columnName)).value;
            val = column.value;
          }
          console.log(val);
          
          this.properties.postSave.message.title.messageTemp = this.properties.postSave.message.title.messageTemp.replace("get("+contador+")",val);
        });
      }
      

      if(this.properties.postSave.update){
        console.log("postSave.update");
        
        // let condition = "";
        // let wa = this.properties.postSave.update.where.replace(/\s/g, '').split("=");
        // let column = wa[1];
        //this.properties.postSave.update.where.split(",").forEach((element:any) => {

          let condition = "";
          this.properties.postSave.update.where.forEach((element2:any) => {
            const map = new Map<string,string>(Object.entries(element2));
              map.forEach((val: string, key: string) => {
              //convMapDetail[key] = val;
              
              this.properties.columnsForm.forEach((element: any) => {
                let column = ""
                if(element.type == 'select'){
                  column = element.tableName + "."+element.relatedBy
                  
                  
                }else{
                  column = element.tableName + "."+element.columnName
                }
                
                if(val == column){
                  let selectValue = (<HTMLInputElement>document.getElementById(val)).value;
                  condition = condition + key + " = " + selectValue + " and ";
                }
              });  
            });
          });



          // column = column.replace(/\s/g, '').split("->")[1];
          // console.log(column);
          // let value ;
          // this.properties.columnsForm.forEach((columnForm:any) => {
            
            
          //   if(columnForm.tableName+"."+columnForm.relatedBy == column){
          //     console.log(columnForm.tableName+"."+columnForm.columnName );
          //     //column = columnForm;
          //     value = (<HTMLInputElement>document.getElementById(columnForm.tableName+"."+columnForm.relatedBy)).value;
          //     //value = columnForm.value;
          //   }
          // });
          // condition = condition + column + " = " + value;
      //});
      //let we = this.properties.postSave.update.columns.params.split("->")[0];
      // let id =  this.properties.id;   
      
      condition = condition.substring(0,condition.length-4)
      console.log(condition);
      this.commonService.update(this.properties.postSave.update.tableName, 
          this.properties.postSave.update.columns,
          condition).subscribe(data => {
            console.log(data);
        });
      }
      let data ;
      if(this.properties.postSave.api){
        this.properties.postSave.api.params.forEach((element:any) => {
            if(element[1] == "this.id") {
              element[1] = this.properties.id;
            }
        });
        console.log(this.properties.postSave.api.params);
        
        data = await this.commonService.genericRequest(this.properties.postSave.api.url,this.properties.postSave.api.params);
        this.properties.ticketOperacion = data.id;
        localStorage.setItem("ticketOperacion", this.properties.ticketOperacion);
        console.log(data);
      }
      let com = this.commonService;
      let wa = this.properties;
      let that = this;
      this.utilService.msgConfirmSaveWithButtons(
          this.properties.postSave.message.title.messageTemp,
          this.properties.postSave.message.icon,
          this.properties.postSave.message.rows).then((result) => {
          
            // this.properties.postSave.message.rows.forEach((element:any) => {
            //   element.columns.forEach((column:any) => {
            //     let element = document.getElementById(column.id);
            //     element?.removeEventListener("click", column.action.name());
            //   });
            // });
            this.router.navigate([this.properties.urlReturn]);
          });
      return data;
    }
      
  }
  makeRequest(url :string,paramMap:any,type:any){
    return this.commonService.genericPostRequest(url,paramMap,type)
  }
  async preSaveDinamic(){
    console.log("preSave");
    if(this.properties.preSave){
      for (const element of this.properties.preSave) {

      //await this.properties.preSave.forEach(async (element:any) => {
        if(element.select){
          let condition = "";
          element.select.where.forEach((element2:any) => {
            const map = new Map<string,string>(Object.entries(element2));
              map.forEach((val: string, key: string) => {
              //convMapDetail[key] = val;
              
              this.properties.columnsForm.forEach((element: any) => {
                let column = ""
                if(element.type == 'select'){
                  column = element.tableName + "."+element.relatedBy
                  
                  
                }else{
                  column = element.tableName + "."+element.columnName
                }
                
                if(val == column){
                  let selectValue = (<HTMLInputElement>document.getElementById(val)).value;
                  condition = condition + key + " = " + selectValue + " and ";
                }
              });  
            });
          });
          
          // let condition = element.select.where.replace(" ","").split("=");
          // console.log(condition);
          
          // let column = condition[0];
          // let value = condition[1].split("->");
          // let selectValue = (<HTMLInputElement>document.getElementById(value[1])).value;
          // console.log(column,selectValue);
          console.log(condition);
          
          if(condition){
            condition = condition.substring(0,condition.length-4);
            console.log(condition);
            let data =  await this.commonService.selectByTableNameAndColumnsAsync(element.select.from, 
              element.select.columns.replace(/ /g,''),
              condition);
              
            console.log(data);
            if(data && data.length > 0){
              for (const column of this.properties.columnsForm) {
                if(column.columnName == element.columnForm){
                  let valueResult = Number(data[0][0]) + 1 ;
                  column.value = valueResult;
                  (<HTMLInputElement>document.getElementById(column.tableName + "."+ column.columnName)).value = valueResult.toString();
                }
              }
            }
          }
          
            
          // };
          // return account();
            // async () => {  
            //   account().then((data:any) =>{
            //     console.log(data);  
            //     this.properties.columnsForm.forEach((column:any) => {
            //       if(column == value[1]){
            //         column.value = data[0];
            //       }
            //     });
            //   });
            // };
          
          
          // promise.then((data)=>{
          //     console.log("Promise resolved with: " + JSON.stringify(data));
          // }) 
          // super.selectByTableNameAndColumns(element.select.from, 
          //   element.select.columns,
          //   column + " = " +selectValue).subscribe(data => {
          //   console.log(data);
          //   this.properties.columnsForm.forEach((column:any) => {
          //     if(column == value[1]){
          //       column.value = data;
          //     }
          //   });
          // });
        }
      }
      //});
    }
    //this.properties.preSave
  }
  async save() {
    console.log("wss");
    let myMap = new Map();
    myMap.set("id", this.properties.id ? this.properties.id : 0);
    this.properties.columnsForm.forEach((element: any) => {
      if (element.type != "label" && element.type != "hidden") {
        let column = "";
        if (element.type == "input" || element.type == "date" || element.type == "input-large" ) {
          column = element?.tableName + "." + element?.columnName;
          let selectValue = (<HTMLInputElement>document.getElementById(column)).value;
          element.value = selectValue;
          myMap.set(element.columnName, selectValue);

        } else if(element.type == "select") {
          column = element?.tableName + "." + element?.relatedBy;
          console.log(column);
          let selectValue = (<HTMLInputElement>document.getElementById(column)).value;
          element.value = selectValue;
          myMap.set(element.relatedBy, selectValue);
        }
        else{
          console.log(element.relatedBy, element.value);
        
          myMap.set(element.relatedBy, element.value);
        }
      }else{
        console.log(element.columnName, element.value);
        
        myMap.set(element.columnName, element.value);
      }
    });
    const convMap: any = {};
    myMap.forEach((val: string, key: string) => {
      convMap[key] = val;
    });
    this.properties.filters = convMap;

    //savind details
    myMap = new Map();
    this.properties.details.forEach((element: any) => {
      console.log(element.listData);
      element.filters = [];
      let contador = -1;
      
      element.listData.forEach((lineData:any) => {
        //en cada linea
        contador++
        let contador2 = -1;
        element.columnsList.forEach((line:any) => {
          contador2++;
          if(line.type != 'none'){
            if(line.type == 'select'){
            
              let column = line?.tableName + "." + line?.relatedBy;
              console.log(column+"_"+contador);
              let selectValue = (<HTMLInputElement>document.getElementById(column+"_"+contador)).value;
              //element.value = selectValue;
              myMap.set(line.relatedBy, selectValue);
            }else{
              myMap.set(line.columnName, lineData[contador2]);
            }
          }
          
          
        });
        console.log(myMap);
        
        const convMapDetail: any = {};
        myMap.forEach((val: string, key: string) => {
          convMapDetail[key] = val;
        });
        element.filters.push(convMapDetail);
      });
      
      
    });
    
    // console.log(this.properties);
    if(this.properties.postSaveTrans){

    }
    // this.properties.details.forEach((a:any) => {
    //   console.log(a.postSaveTrans);
    //   if(a.postSaveTrans){
    //     a.postSaveTrans.forEach((e:any) => {
    //       if(e.type == 'update'){
    //         console.log("afre");
            
    //         let columns = ""
    //         let condition = ""
    //         // e.values?.forEach((element:any) => {
    //         //   const map = new Map(Object.entries(element));
    //         //   columns = columns + element + ",";
    //         // });
    //         e.where?.forEach((element2:any) => {
    //           const map = new Map<string,string>(Object.entries(element2));
    //           map.forEach((val: string, key: string) => {
    //             //convMapDetail[key] = val;
    //             console.log(val);
                
    //             let selectValue = (<HTMLInputElement>document.getElementById(val)).value;
    //             condition = condition + key + " = " + selectValue + " and ";
    //           });
    //         });
    //         let up = new UpdateParam()
    //         up.columns = columns
    //         up.tableName = e.tableName
    //         up.condition = condition.substring(0,condition.length-4)
    //         a.updateParam = up;
    //         //up.columns = 
    //         //e.updateParam = 
    //       }
          
        
    //     });
    //     console.log(this.properties);
    //   }
    // });
    console.log(this.properties);
    
    let wa = await this.commonService.createTransactionalAsync(this.properties.api, this.properties)
    .catch(err => {
        if (err.status === 422) {
          console.log("error..");
          
          this.error = err.error;
        }
    });
    console.log(wa);
    if(wa){
      const map = new Map(Object.entries(wa));
      this.properties.id = map.get(this.properties.tableName +  "_id");
    }
    return wa;
      //this.utilService.msgConfirmSaveWithButtons(this.properties.filters["serie"]+ '-' +this.properties.filters["numero"]);
      
    // }, err => {

    //   //error en validaciones, los demas errores en JwtInterceptor
    //   if (err.status === 422) {
    //     this.error = err.error;
    //   }
    //   console.log(this.error[0]);
    // });
  }
  // viewDetail(object: any) {
  //   if(!this.modalsNumber || this.modalsNumber == 1){
  //     this.modalRef = this.modalService.open(GenericModalComponent, { centered: true });
  //     // this.modalRef.componentInstance.cnfBpartnerId = element.cnfBpartnerId;
  //     this.modalRef.componentInstance.headers = ["Artículo", "Cantidad", "Valor unitario", "Subtotal","Factura"];
  //     this.modalRef.componentInstance.listDetail = [];
  //     this.modalRef.componentInstance.message = "Documento "+this.properties.filters["serie"]+ "-" +this.properties.filters["numero"]+" generado correctamente"
  //     this.modalRef.componentInstance.result.subscribe((receivedEntry: any) => {
  //       console.log(receivedEntry);
  //       //object.listInvDocumentLine = receivedEntry;
  //     });
  //   }
    
  // }
  delete(id:number){

  }
  onChangeinDetail(method:any,idxLine:any,event: any){
    console.log(method,idxLine,event);
    if(method){
      let line = this.properties.details[0].listData[idxLine];
      let contador = -1;
      this.properties.details[0].listData[idxLine].forEach((lineItem:any) => {
          //en cada linea
          
          contador++
          let columnItem = this.properties.details[0].columnsList[contador];
            // console.log(columnItem);
            
            if (columnItem && (columnItem.type == "input" || columnItem.type == "date" || columnItem.type == "input-large" )) {
              let column = columnItem?.tableName + "." + columnItem?.columnName;
              
              if(event.srcElement.id == column+"_"+idxLine){
                console.log(event.srcElement.id,column);
              
                let selectValue = (<HTMLInputElement>document.getElementById(column+"_"+idxLine)).value;
                console.log(lineItem,selectValue);
                this.properties.details[0].listData[idxLine][contador] = selectValue;
                //event.preventDefault();
                //(<HTMLInputElement>document?.getElementById(column+"_"+idxLine)).focus();
                //event.preventDefault();
              }
              
            }
            
            //myMap.set(line.columnName, lineData[contador++]);
        });
      console.log(this.properties);
      
      method.name(this.properties,idxLine);
      
    }
    
  }
  getListAsObservable(term: any,item:any):Observable<any>{
    console.log(term,item);
    let condition:any = "";
      if (term.length >= 2) {
        return this.commonService.selectByTableNameAndColumns(item.search.tableName, 
          item.search.columnName,
          item.search.where.replace(["[value]"],"'"+term+"'").replace(["[value]"],"'"+term+"'")).pipe(
          tap((data) => {
            console.log(data);
            //item.listData.push(data[0]);
            this.searchFailed = false
            
            //postFunction.name(this.properties);
          }),
          catchError(() => {
            this.searchFailed = true;
            return of([]);
          })
        );
        
      } else {
        return <any>[];
      }
    
  }
  getListInDetailsAsObservable(term: any,itemLine:any):Observable<any>{
    console.log(term,itemLine);
    let condition:any = "";
      if (term.length >= 2) {
        return this.commonService.selectByTableNameAndColumns(itemLine.search.tableName, 
          itemLine.search.columnName,
          itemLine.search.where.replace(["[value]"],"'"+term+"'").replace(["[value]"],"'"+term+"'")).pipe(
          tap((data) => {
            console.log(data);
            //item.listData.push(data[0]);
            this.searchFailed = false
            
            //postFunction.name(this.properties);
          }),
          catchError(() => {
            this.searchFailed = true;
            return of([]);
          })
        );
        
      } else {
        return <any>[];
      }
    // }
    // else{return <any>[];}
    
  }
  pdf(){
    console.log("pdf");
    
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
  onchangeInForm(item:any,event:any){
    item.value = event.item[0]
    console.log(event.item);
  }
  // public searchFactory(postFunction:any, item:any): (text$: Observable<string>) => Observable<any[]> {
  //   console.log("wecs");
    
  //   let  search = (text$: Observable<string>) =>
  //   text$.pipe(
  //     debounceTime(200),
  //     distinctUntilChanged(),
  //     switchMap(term => {
  //       console.log("esg");
  //       console.log(term);
        
  //       return this.getListCnfProductAsObservable(term,postFunction,item);
  //     })
  //   )
  //   return search;
  // }
  
  // select(columns:any,tables:any){
  //    super.selectByTableNameAndColumns(tables, columns).subscribe(data => {
  //     console.log(data);
  //     data.push([0, "- Seleccione -"]);
  //     data.sort();
  //     return data;
  //   });
  // }
}

