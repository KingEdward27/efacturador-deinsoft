import { SegAccionService } from '@/business/service/seg-accion.service';
import { Component, HostListener, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { UtilService } from '@services/util.service';
import { SegAccion } from '../../../../business/model/seg-accion.model';


@Component({
  selector: 'app-seg-accion-form',
  templateUrl: './seg-accion-form.component.html',
  styleUrls: ['./seg-accion-form.component.css']
})
export class SegAccionFormComponent implements OnInit {

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
  public model: SegAccion = new SegAccion();
  protected redirect: string = "/seg-accion";
  selectedOption: any;
  passwordRepeat: any;

  constructor(private segAccionService: SegAccionService,
    private router: Router,
    private utilService: UtilService,
    private route: ActivatedRoute,) {
  }
  ngOnInit(): void {
    this.isDataLoaded = false;
    this.loadData();
  }
  getBack() {
    this.router.navigate([this.redirect]);
  }
  loadData() {
    return this.route.paramMap.subscribe(params => {
      this.id = params.get('id')!;
      if (!this.id) {
        this.isDataLoaded = true;
      }
      if (this.id) {
        this.segAccionService.getData(this.id).subscribe(data => {
          this.model = data;
          console.log(this.model);
          this.isDataLoaded = true;
          //this.titulo = 'Editar ' + this.nombreModel;
        });
      }

    })

  }
  public save(): void {
    this.segAccionService.save(this.model).subscribe(m => {
      this.isOk = true;
      this.utilService.msgOkSave()
      this.router.navigate([this.redirect]);
    }, err => {
      if (err.status === 400) {
        this.error = err.error;
        console.log(this.error);
      }
    });
  }
}

