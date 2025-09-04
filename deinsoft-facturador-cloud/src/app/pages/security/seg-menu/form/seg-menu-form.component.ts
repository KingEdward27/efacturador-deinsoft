import { SegMenu } from '@/business/model/seg-menu.model';
import { SegMenuService } from '@/business/service/seg-menu.service';
import { Component, HostListener, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { UtilService } from '@services/util.service';


@Component({
  selector: 'app-seg-menu-form',
  templateUrl: './seg-menu-form.component.html'
})
export class SegMenuFormComponent implements OnInit {

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
  public model: SegMenu = new SegMenu();
  selectDefaultSegMenu: any = { id: 0, name: "- Seleccione -" }; listSegMenu: any;
  segMenu: SegMenu = new SegMenu();
  loadingSegMenu: boolean = false;
  protected redirect: string = "/menu";
  selectedOption: any;
  passwordRepeat: any;

  constructor(private segMenuService: SegMenuService,
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
    this.getListSegMenu();
    return this.route.paramMap.subscribe(params => {
      this.id = params.get('id')!;
      console.log(this.id);
      if (!this.id) {
        this.isDataLoaded = true;
      }
      if (this.id) {
        this.segMenuService.getData(this.id).subscribe(data => {
          this.model = data;
          console.log(this.model);
          if (this.model.parent == null) {
            this.model.parent = this.selectDefaultSegMenu;
          }
          this.isDataLoaded = true;
          //this.titulo = 'Editar ' + this.nombreModel;
        });
      }

    })

  }
  public save(): void {
    this.segMenuService.save(this.model).subscribe(m => {
      console.log(m);
      this.isOk = true;
      this.utilService.msgOkSave()
      this.router.navigate([this.redirect]);
    }, err => {
      if (err.status === 422) {
        this.error = err.error;
        console.log(this.error);
      }
    });
  }
  getListSegMenu() {
    this.loadingSegMenu = true;
    console.log(this.chargingsb);
    return this.segMenuService.getAllDataCombo().subscribe(data => {
      this.listSegMenu = data;
      this.loadingSegMenu = false;
    })

  }
  compareSegMenu(a1: SegMenu, a2: SegMenu): boolean {
    if (a1 === undefined && a2 === undefined) {
      return true;
    }

    return (a1 === null || a2 === null || a1 === undefined || a2 === undefined)
      ? false : a1.id === a2.id;
  }
}

