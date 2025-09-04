
import { Component, OnInit } from '@angular/core';
import { DataTableDirective } from 'angular-datatables';
import { Router } from '@angular/router';
import { SegMenu } from '@/business/model/seg-menu.model';
import { SegMenuService } from '@/business/service/seg-menu.service';
import { UtilService } from '@services/util.service';
@Component({
  selector: 'app-seg-menu-list',
  templateUrl: './seg-menu-list.component.html',
  styleUrls: ['./seg-menu-list.component.css']
})
export class SegMenuListComponent implements OnInit {
  lista: any;
  datatableElement!: DataTableDirective;
  dtOptions: DataTables.Settings = {};
  nameSearch: string = "";
  modelSearch: SegMenu = new SegMenu();
  dataTable!: DataTables.Api;
  constructor(private segMenuService: SegMenuService, private utilService: UtilService, private router: Router) { }

  ngOnInit(): void {
    this.getAllData();
  }
  public getAllDataByFilters() {
    this.segMenuService.getAllData(this.modelSearch)
      .subscribe(data => {
        this.lista = data;
      });
  }
  public getAllData() {
    this.segMenuService.getAllData(this.modelSearch).subscribe(data => {
      this.lista = data;
      setTimeout(() => {
        this.dataTable = $('#dtDataSegMenu').DataTable(this.utilService.datablesSettings);
      }, 0);
      console.log(data);
      this.dataTable?.destroy();
    });
  }
  editar(e: SegMenu) {
    if (this.utilService.validateDeactivate(e)) {
      this.router.navigate(["/new-menu", { id: e.id }]);
    }

  } eliminar(e: SegMenu) {
    this.utilService.confirmDelete(e).then((result) => {
      if (result) {
        this.segMenuService.delete(e.id.toString()).subscribe(() => {
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
