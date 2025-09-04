import { SegAccion } from '@/business/model/seg-accion.model';
import { SegMenu } from '@/business/model/seg-menu.model';
import { SegPermiso } from '@/business/model/seg-permiso.model';
import { SegRol } from '@/business/model/seg-rol.model';
import { SegAccionService } from '@/business/service/seg-accion.service';
import { SegMenuService } from '@/business/service/seg-menu.service';
import { SegPermisoService } from '@/business/service/seg-permiso.service';
import { SegRolService } from '@/business/service/seg-rol.service';
import { Component, HostListener, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { UtilService } from '@services/util.service';
import { filter } from 'rxjs/operators';


@Component({
  selector: 'app-seg-permiso-form2',
  templateUrl: './seg-permiso-form2.component.html'
})
export class SegPermisoForm2Component implements OnInit {

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
  public model: SegPermiso = new SegPermiso();
  selectDefaultSegRol: any = { id: 0, name: "- Seleccione -" }; listSegRol: any;
  segRol: SegRol = new SegRol();
  loadingSegRol: boolean = false;
  selectDefaultSegMenu: any = { id: 0, name: "- Seleccione -" }; listSegMenu: any;
  segMenu: SegMenu = new SegMenu();
  loadingSegMenu: boolean = false;
  selectDefaultSegAccion: any = { id: 0, name: "- Seleccione -" }; listSegAccion: any;
  segAccion: SegAccion = new SegAccion();
  loadingSegAccion: boolean = false;
  protected redirect: string = "/permiso";
  selectedOption: any;
  passwordRepeat: any;
  lista: any;
  dataTable!: DataTables.Api;

  constructor(private segPermisoService: SegPermisoService,
    private router: Router,
    private utilService: UtilService,
    private segRolService: SegRolService,
    private segMenuService: SegMenuService,
    private segAccionService: SegAccionService,
    private route: ActivatedRoute,) {
  }
  ngOnInit(): void {
    this.isDataLoaded = false;
    this.loadData();
  }
  getBack() {
    this.router.navigate([this.redirect]);
  }
  public getAllData() {
    this.segPermisoService.getAllData(this.model).subscribe(data => {
      console.log(data);
      
      this.lista = data;
      
    this.lista = this.lista.filter(data => data.segRol.id == this.model.segRol.id).filter(data => data.segMenu.parent != null)
    this.lista.forEach(permiso => {
      permiso.check = false;
      this.listSegAccion.forEach(accion => {
        if (permiso.segAccion.id == accion.id) {
          permiso.check = true;
        }
      });
    });
      this.dataTable?.destroy();
      setTimeout(() => {
        this.dataTable = $('#dtDataSegPermiso').DataTable(this.utilService.datablesSettings);
      }, 0);
    });
  }
  loadData() {
    this.getListSegRol();
    this.getListSegMenu();
    this.getListSegAccion();
    return this.route.paramMap.subscribe(params => {
      this.id = params.get('id')!;
      console.log(this.id);
      if (!this.id) {
        this.isDataLoaded = true;
      }
      if (this.id) {
        this.segPermisoService.getData(this.id).subscribe(data => {
          this.model = data;
          console.log(this.model);
          this.isDataLoaded = true;
          //this.titulo = 'Editar ' + this.nombreModel;
        });
      }

    })

  }
  public save(): void {
    this.segPermisoService.save(this.model).subscribe(m => {
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
  getListSegRol() {
    this.loadingSegRol = true;
    return this.segRolService.getAllDataCombo().subscribe(data => {
      this.listSegRol = data;
      this.loadingSegRol = false;
    })

  }
  compareSegRol(a1: SegRol, a2: SegRol): boolean {
    if (a1 === undefined && a2 === undefined) {
      return true;
    }

    return (a1 === null || a2 === null || a1 === undefined || a2 === undefined)
      ? false : a1.id === a2.id;
  }
  getListSegMenu() {
    this.loadingSegMenu = true;
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
  getListSegAccion() {
    this.loadingSegAccion = true;
    return this.segAccionService.getAllDataCombo().subscribe(data => {
      this.listSegAccion = data;
      this.loadingSegAccion = false;
    })

  }
  compareSegAccion(a1: SegAccion, a2: SegAccion): boolean {
    if (a1 === undefined && a2 === undefined) {
      return true;
    }

    return (a1 === null || a2 === null || a1 === undefined || a2 === undefined)
      ? false : a1.id === a2.id;
  }
  getListData (item: any) {
    console.log(item);
    console.log(this.listSegMenu);
    
    this.getAllData()
  }
}

