import { AfterViewInit, Component, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
// import { CustomAdapter, CustomDateParserFormatter } from 'src/app/util/CustomDate';
import { NgbActiveModal, NgbCalendar, NgbDateAdapter, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';




@Component({
  selector: 'app-generic-modal',
  templateUrl: './generic-modal.component.html'
})
export class GenericModalComponent implements OnInit {

  //generic variables
  error: any;
  headers :any;
  listDetail: any;
  load:boolean = false;
  listDetail2: any[][] = [];
  message:any;
  @Output() result: EventEmitter<any> = new EventEmitter();
  constructor(public activeModal: NgbActiveModal) {
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
    for( var i=0; i<this.listDetail.length; i++ ) {
      this.listDetail2.push( [] );
    }
    
    for (let index = 0; index < this.listDetail.length; index++) {
      const element = this.listDetail[index];
      let arr:any = [];  
      Object.keys(element).map(function(key){  
          arr.push(element[key]);
          console.log(element);
      });
      this.listDetail2[index].push(arr);
      
    }
    this.load = true;
    console.log(this.listDetail2);
  }
  save(){
    this.result.emit(this.listDetail);
    this.activeModal.close();
  }
}

