import { UpdateParam } from '@/base/components/model/UpdateParam';
import { CommonService } from '@/base/services/common.service';
import { AfterViewInit, Component, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
// import { CustomAdapter, CustomDateParserFormatter } from 'src/app/util/CustomDate';
import { NgbActiveModal, NgbCalendar, NgbDateAdapter, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { environment } from 'environments/environment';




@Component({
  selector: 'app-message-modal',
  templateUrl: './message-modal.component.html'
})
export class MessageModalComponent implements OnInit {

  //generic variables
  error: any;
  headers :any;
  listDetail: any;
  load:boolean = false;
  listDetail2: any[][] = [];
  message:any;
  @Output() result: EventEmitter<any> = new EventEmitter();
  public id = 0;
  constructor(public activeModal: NgbActiveModal,private commonService: CommonService) {
    this.commonService.baseEndpoint = environment.apiUrl;
  }
  ngOnInit(): void {
    this.load = false;
    console.log(this.headers);
    console.log(this.listDetail);
    // this.listDetail.forEach((element:any) => {
    //   let arr:any = [];  
    //   Object.keys(element).map(function(key){  
    //       arr.push({[key]:element[key]});
    //       console.log(element);
    //   });
    //   this.listDetail2.push(arr);
    // });
    // for( var i=0; i<this.listDetail.length; i++ ) {
    //   this.listDetail2.push( [] );
    // }
    
    // for (let index = 0; index < this.listDetail.length; index++) {
    //   const element = this.listDetail[index];
    //   let arr:any = [];  
    //   Object.keys(element).map(function(key){  
    //       arr.push(element[key]);
    //       console.log(element);
    //   });
    //   this.listDetail2[index].push(arr);
      
    // }
    this.load = true;
    console.log(this.listDetail2);
  }
  save(){
    this.result.emit(this.listDetail);
    this.activeModal.close();
  }
  ticketChild(){
    console.log("arfas");
    
    let myMap = new Map();
    myMap.set("id", this.id);
    myMap.set("tipo", 2);
    let mp = new UpdateParam();
    const convMapDetail: any = {};
    myMap.forEach((val: string, key: string) => {
      convMapDetail[key] = val;
    });
    console.log(convMapDetail);
    mp.map = convMapDetail;
    this.commonService.updateParam = mp;
    
    this.commonService.genericPostRequest("/backend/get-pdf?id="+this.id+'&tipo='+2, mp, 'blob').subscribe(data => {
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
    myMap.set("id", this.id);
    myMap.set("tipo", 1);
    let mp = new UpdateParam();
    const convMapDetail: any = {};
    myMap.forEach((val: string, key: string) => {
      convMapDetail[key] = val;
    });
    console.log(convMapDetail);

    mp.map = convMapDetail;
    this.commonService.genericPostRequest("/backend/get-pdf?id="+this.id+'&tipo='+1, mp, 'blob').subscribe(data => {
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

