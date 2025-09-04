import { Component, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { NgbActiveModal, NgbCalendar, NgbDateAdapter, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { UtilService } from '../../services/util.service';
import { CnfZona } from '../../business/model/cnf-zona';
import { CnfZonaService } from '../../business/service/cnf-zona.service';
import { CnfEmpresa } from '../../business/model/cnf-empresa.model';
import { AppService } from '../../services/app.service';



@Component({
  selector: 'ngbd-modal-content',
  templateUrl: './ope-generic-form-modal.component.html'
})
export class OpeGenericFormModalComponent implements OnInit {

  //generic variables
  error: any;
  selectedItemsList = [];
  checkedIDs = [];
  chargingsb: boolean = true;
  isDataLoaded: Boolean = false;
  isOk: boolean = false;
  isWarning: boolean = false;
  isDanger: boolean = false;
  message: any = "";
  id: string = "";

  //variables propias
  public model: any;
  selectedOption: any;
  passwordRepeat: any;
  @Input() public form:string;
  @Output() registerOut: EventEmitter<any> = new EventEmitter();
  constructor(
    private router: Router,
    private utilService: UtilService,
    public activeModal: NgbActiveModal,private cnfZonaService: CnfZonaService, private appService: AppService) {
  }
  ngOnInit(): void {
    console.log(this.form);
    
    if (this.form == 'CnfZona') {
      this.model = new CnfZona();
    }
    this.isDataLoaded = false;
    // this.loadData();
  }
  public save(): void {
    console.log(this.model);
    let cnfEmpresa = this.appService.getProfile().profile.split("|")[1];
    if(cnfEmpresa == '*') {
    } else {
      this.model.cnfEmpresa.id = cnfEmpresa;
    }
    if (this.form == 'CnfZona') {
      this.cnfZonaService.save(this.model).subscribe(m => {
        console.log(m);
        this.isOk = true;
        this.utilService.msgOkSave();
        this.registerOut.emit(m);
        this.activeModal.close();
      }, err => {
        if (err.status === 422) {
          this.error = err.error;
          console.log(this.error);
        }
      });
    }
  }
}

