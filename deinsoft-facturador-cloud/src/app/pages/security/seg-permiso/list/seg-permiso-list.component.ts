
import { Component, OnInit } from '@angular/core';
import { DataTableDirective } from 'angular-datatables';
import { Router } from '@angular/router';
import { UtilService } from '@services/util.service';
import { SegPermisoService } from '@/business/service/seg-permiso.service';
import { SegPermiso } from '@/business/model/seg-permiso.model';
@Component({
  selector: 'app-seg-permiso-list',
  templateUrl: './seg-permiso-list.component.html'
})
export class SegPermisoListComponent implements OnInit {
  lista: any;
  datatableElement!: DataTableDirective;
  dtOptions: DataTables.Settings = {};
  nameSearch: string = "";
  modelSearch: SegPermiso = new SegPermiso();
  dataTable!: DataTables.Api;
  constructor(private segPermisoService: SegPermisoService, private utilService: UtilService, private router: Router) { }

  ngOnInit(): void {
    this.getAllData();
  }
  public getAllDataByFilters() {
    this.segPermisoService.getAllData(this.modelSearch)
      .subscribe(data => {
        this.lista = data;
      });
  }
  public getAllData() {
    this.segPermisoService.getAllData(this.modelSearch).subscribe(data => {
      this.lista = data;
      setTimeout(() => {
        this.dataTable = $('#dtDataSegPermiso').DataTable(this.utilService.datablesSettings);
      }, 0);
      console.log(data);
      this.dataTable?.destroy();
    });
  }
  editar(e: SegPermiso) {
    if (this.utilService.validateDeactivate(e)) {
      this.router.navigate(["/new-permiso", { id: e.id }]);
    }

  }
  eliminar(e: SegPermiso) {
    this.utilService.confirmDelete(e).then((result) => {
      if (result) {
        this.segPermisoService.delete(e.id.toString()).subscribe(() => {
          this.utilService.msgOkDelete();
          this.getAllData();
        }, err => {
          if (err.status === 500 && err.error.trace.includes("DataIntegrityViolationException")) {
            this.utilService.msgProblemDelete();
          }
        });
      }

    });
  }
}
