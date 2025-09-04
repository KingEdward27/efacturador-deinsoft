
import { Component, OnInit } from '@angular/core';
import { DataTableDirective } from 'angular-datatables';
import { SegAccion } from '../../../../business/model/seg-accion.model';
import { Router } from '@angular/router';
import { SegAccionService } from '@/business/service/seg-accion.service';
import { UtilService } from '@services/util.service';
@Component({
  selector: 'app-seg-accion-list',
  templateUrl: './seg-accion-list.component.html',
  styleUrls: ['./seg-accion-list.component.css']
})
export class SegAccionListComponent implements OnInit {
  lista: any;
  datatableElement!: DataTableDirective;
  dtOptions: DataTables.Settings = {};
  nameSearch: string = "";
  modelSearch: SegAccion = new SegAccion();
  dataTable!: DataTables.Api;
  constructor(private segAccionService: SegAccionService, private utilService: UtilService, private router: Router) { }

  ngOnInit(): void {
    this.getAllData();
  }
  public getAllDataByFilters() {
    this.segAccionService.getAllData(this.modelSearch)
      .subscribe(data => {
        this.lista = data;
      });
  }
  public getAllData() {
    this.segAccionService.getAllData(this.modelSearch).subscribe(data => {
      this.lista = data;
      setTimeout(() => {
        this.dataTable = $('#dtDataSegAccion').DataTable(this.utilService.datablesSettings);
      }, 0);
      console.log(data);
      this.dataTable?.destroy();
    });
  }
  editar(e: SegAccion) {
    if (this.utilService.validateDeactivate(e)) {
      this.router.navigate(["/new-accion", { id: e.id }]);
    }

  } eliminar(e: SegAccion) {
    this.utilService.confirmDelete(e).then((result) => {
      if (result) {
        this.segAccionService.delete(e.id.toString()).subscribe(() => {
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
