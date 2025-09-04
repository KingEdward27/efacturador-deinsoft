import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbCalendar, NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { DataTableDirective } from 'angular-datatables';
import * as dayjs from 'dayjs';
import { environment } from 'environments/environment';
import Swal from 'sweetalert2';
import { GenericMasterDetailFormComponent } from '../base/components/generic-master-detail-form/generic-master-detail-form.component';
import { Status } from '../base/interfaces/State';
import { CommonService } from '../base/services/common.service';

@Injectable({
  providedIn: 'root'
})
export class UtilService {
  datablesSettingsChild!: DataTables.Settings;
  dtOptions: any;
  datablesSettings: any
  datablesSettingsNoOrderable: any;
  // public listReports:any[] = [];
  url: string = environment.apiUrl;
  public titleExport = ""
  constructor(private http: HttpClient, private router: Router, private translate: TranslateService,
    public ngbCalendar: NgbCalendar, public dateAdapter: NgbDateAdapter<dayjs.Dayjs>) {
    let lang = localStorage.getItem('lang');
    console.log(lang);
    // if (rememberUser != "null") {
    //   this.profile = rememberUser ? <SecRole>JSON.parse(rememberUser).profile : new SecRole();
    //   // this.tenant = rememberUser ? <CnfTenant>JSON.parse(rememberUser).tenant : new CnfTenant();
    //   // this.company = rememberUser ? <CnfCompany>JSON.parse(rememberUser).company : new CnfCompany();
    //   // this.org = rememberUser ? <CnfOrg>JSON.parse(rememberUser).org : new CnfOrg();
    //   console.log(this.profile);
    // }

    this.datablesSettings = {
      deferRender: true,
      deferLoading: 7,
      pagingType: 'full_numbers',
      searching: false,
      processing: true,
      lengthMenu: [25, 50, 100],
      order: [[0, "asc"]],
      dom: 'lBftip',
      buttons: [{
        extend: 'excel',
        title: this.titleExport,
        text: '<i class="fa fa-file-excel"></i>&nbsp; XLS',
        exportOptions: {
          columns: ':visible'
        }
      },
      {
        extend: 'pdf',
        title: this.titleExport,
        text: ' <i class="fa fa-file-pdf"></i>&nbsp; PDF',
        orientation: 'landscape'
      },
      {
        extend: 'csv',
        title: this.titleExport,
        text: '<i class="fa fa-file-excel"></i>&nbsp; CSV',
        exportOptions: {
          columns: ':visible'
        }
      },

      ],
      language: {
        url: 'assets/i18n/datatables/lang' + lang?.toUpperCase() + '.json'
        // emptyTable: '',
        // zeroRecords: 'No hay coincidencias',
        // lengthMenu: 'Mostrar _MENU_ elementos',
        // search: 'Buscar:',
        // info: 'De _START_ a _END_ de _TOTAL_ elementos',
        // infoEmpty: 'De 0 a 0 de 0 elementos',
        // infoFiltered: '(filtrados de _MAX_ elementos totales)',
        // paginate: {
        //   first: 'Prim.',
        //   last: 'Últ.',
        //   next: 'Sig.',
        //   previous: 'Ant.'
        // }
      },
      responsive: true
    }
    this.datablesSettingsNoOrderable = {
      deferRender: true,
      deferLoading: 7,
      pagingType: 'full_numbers',
      searching: false,
      processing: true,
      lengthMenu: [25, 50, 100],
      ordering: false,
      dom: 'lBftip',
      buttons: [{
        extend: 'excel',
        title: this.titleExport,
        text: '<i class="fa fa-file-excel"></i>&nbsp; XLS',
        exportOptions: {
          columns: ':visible'
        }
      },
      {
        extend: 'pdf',
        title: this.titleExport,
        text: ' <i class="fa fa-file-pdf"></i>&nbsp; PDF',
        orientation: 'landscape'
      },
      {
        extend: 'csv',
        title: this.titleExport,
        text: '<i class="fa fa-file-excel"></i>&nbsp; CSV',
        exportOptions: {
          columns: ':visible'
        }
      },

      ],
      language: {
        url: 'assets/i18n/datatables/lang' + lang?.toUpperCase() + '.json'
        // emptyTable: '',
        // zeroRecords: 'No hay coincidencias',
        // lengthMenu: 'Mostrar _MENU_ elementos',
        // search: 'Buscar:',
        // info: 'De _START_ a _END_ de _TOTAL_ elementos',
        // infoEmpty: 'De 0 a 0 de 0 elementos',
        // infoFiltered: '(filtrados de _MAX_ elementos totales)',
        // paginate: {
        //   first: 'Prim.',
        //   last: 'Últ.',
        //   next: 'Sig.',
        //   previous: 'Ant.'
        // }
      }
    }
    this.datablesSettingsChild = {
      deferRender: true,
      deferLoading: 7,
      paging: false,
      searching: true,
      info: false,
      processing: true,
      order: [[0, "asc"]],
      autoWidth: false,
      dom: 'lBftip',
      language: {
        url: 'assets/i18n/datatables/lang' + lang?.toUpperCase() + '.json'
        // emptyTable: '',
        // zeroRecords: 'No hay coincidencias',
        // lengthMenu: 'Mostrar _MENU_ elementos',
        // search: 'Buscar:',
        // info: 'De _START_ a _END_ de _TOTAL_ elementos',
        // infoEmpty: 'De 0 a 0 de 0 elementos',
        // infoFiltered: '(filtrados de _MAX_ elementos totales)',
        // paginate: {
        //   first: 'Prim.',
        //   last: 'Últ.',
        //   next: 'Sig.',
        //   previous: 'Ant.'
        // }
      }
    }
  }
  get today() {
    return this.dateAdapter.toModel(this.ngbCalendar.getToday())!;
  }
  public getUrl(): string {
    return this.url;
  }

  public setUrl(url: string): void {
    this.url = url;
  }

  /**
   * @see print fields that has incorrect values and invalidate some form
   * @param form
   */
  public findInvalidControls(form: FormGroup) {
    const invalid = [];
    const controls = form.controls;
    for (const name in controls) {
      if (controls[name].invalid) {
        invalid.push(name);
      }
    }
    console.log(invalid);
  }
  public setDatatable(dataTable: any) {

  }
  async confirmDelete(e: any): Promise<boolean> {
    console.log(e);
    let resultConfirm: boolean = false;
    await Swal.fire({
      title: 'Cuidado:',
      text: `¿Seguro que desea eliminar?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Si, Confirmar!'
    }).then((result) => {
      resultConfirm = result.value;
    })
    console.log(resultConfirm);
    return resultConfirm;
  }
  async confirmOperation(title: any): Promise<boolean> {
    console.log(title);
    let resultConfirm: boolean = false;
    await Swal.fire({
      title: title?title:"Cuidado!",
      text: `¿Seguro que desea realizar esta operación?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Si, Confirmar!'
    }).then((result) => {
      resultConfirm = result.value;
    })
    console.log(resultConfirm);
    return resultConfirm;
  }
  validateDeactivate(e: any) {
    if (e.state == 0) {
      Swal.fire('Inactivación de registro:', `El registro ya se encuentra inactivo`, 'warning');
      return false;
    } else {
      return true
    }
  }
  validateStatusToProcess(e: any) {
    if (e.status == Status.COMPLETADO) {
      Swal.fire('Edición de Registro:', `No se puede realizar esta operación a un registro en estado COMPLETADO`, 'warning');
      return false;
    } if (e.status == Status.ANULADO) {
      Swal.fire('Edición de Registro:', `No se puede realizar esta operación a un registro en estado ANULADO`, 'warning');
      return false;
    } else {
      return true
    }
  }
  validateStatusToInvalidate(e: any) {
    if (e.status == Status.REGISTRADO) {
      Swal.fire('Edición de Registro:', `No se puede realizar esta operación a un registro en estado REGISTRADO`, 'warning');
      return false;
    } if (e.status == Status.ANULADO) {
      Swal.fire('Edición de Registro:', `No se puede realizar esta operación a un registro en estado ANULADO`, 'warning');
      return false;
    } else {
      return true
    }
  }
  async confirmInactive(e: any): Promise<boolean> {
    console.log(e);
    if (e.state == 0) {
      Swal.fire('Inactivación de registro:', `El registro ya se encuentra inactivo`, 'warning');
      return false;
    }
    let resultConfirm: boolean = false;
    await Swal.fire({
      title: 'Cuidado:',
      text: `¿Seguro que desea inactivar?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Si, Confirmar!'
    }).then((result) => {
      resultConfirm = result.value;
    });
    console.log(resultConfirm);
    return resultConfirm;
  }
  async confirmProcess(message: any): Promise<any> {
    try {
      let result = await Swal.fire({
        title: 'Cuidado:',
        text: message,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si, Confirmar!'
      });
      return result;
    } catch (e) {
      console.error(e);
      return e;
    }
  }
  async confirmProcessWithMessage(message: any): Promise<boolean> {
    let resultConfirm: boolean = false;
    await Swal.fire({
      title: 'Cuidado:',
      text: message,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Si, Confirmar!'
    }).then((result) => {
      resultConfirm = result.value;
    });
    console.log(resultConfirm);
    return resultConfirm;
  }
  async confirmProcessWithReturn(title: any, label: any) : Promise<string>{
    const { value: value } = await Swal.fire({
      title: title,
      input: 'password',
      inputLabel: label,
      showCancelButton: true,
      inputValue: "",
      inputAttributes: {
        maxlength: "10",
        autocapitalize: 'off',
        autocorrect: 'off',
        autocomplete: "new-password",
        "data-lpignore": "true"
      },
      inputValidator: (value) => {
        if (!value) {
          return 'You need to write something!'
        }
      }
    })
    return value;
  }
  async confirmProcessAjax(title: any, label: any){
    Swal.fire({
      title: 'Submit your Github username',
      input: 'text',
      inputAttributes: {
        autocapitalize: 'off'
      },
      showCancelButton: true,
      confirmButtonText: 'Look up',
      showLoaderOnConfirm: true,
      preConfirm: (login) => {
        return fetch(`//api.github.com/users/${login}`)
          .then(response => {
            if (!response.ok) {
              throw new Error(response.statusText)
            }
            return response.json()
          })
          .catch(error => {
            Swal.showValidationMessage(
              `Request failed: ${error}`
            )
          })
      },
      allowOutsideClick: () => !Swal.isLoading()
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire({
          title: `${result.value.login}'s avatar`,
          imageUrl: result.value.avatar_url
        })
      }
    })
  }
  msgOkSave() {
    Swal.fire('Registro', `Grabado con éxito`, 'success');
  }
  msgConfirmSaveWithButtons(title: string, icon: any, lines: any) {
    let html: string = "";
    console.log(lines);
    if (lines) {
      for (let items of lines) {
        html = html + '<div class="form-group row">' +

          '<div class="col-sm-12" style="padding-top: 20px;">' +
          '    <div class="row justify-content-center">';
        '<ng-container>'
        for (let index = 0; index < items.columns.length; index++) {
          const element = items.columns[index];
          console.log(element);

          html = html + '<div class="col-lg-6  col-sm-6 col-xs-5" style="margin-left: 50x;">' +
            '            <a id="' + element.id + '" class="btn btn-default col-12">' +
            '                <i class="' + element.icon + '"></i> ' + element.value +
            '            </a>' +
            '        </div>';
        }

        // '        <div class="col-lg-6  col-sm-6 col-xs-5" style="margin-left: 50x;">'+
        // '            <a id="ticket" onclick = '+items.action.name()+' class="btn btn-default col-12">'+
        // '                <i class="fa fa-arrow-circle-left"></i> Ticket'+
        // '            </a>'+
        // '        </div>'+
        // '        <div class="col-lg-6 col-sm-6 col-xs-5" style="margin-left: 20x;">'+
        // '            <a id="a4" class="btn btn-primary col-12">'+
        // '                <i class="fa fa-arrow-circle-left"></i> A4'+
        // '            </a>'+
        // '        </div>'+
        html = html + '    </div>' +
          '</div>' +
          '</div>';
      }
    }


    return Swal.fire({
      title: title,
      icon: icon,
      html: '<div class="table-responsive">' +
        '<div class="col-sm-12" style="padding-top: 6px;">' +
        html +
        '</div>' +
        '</div>',
      showCloseButton: true,
      allowOutsideClick: false,
      showConfirmButton: true,

      confirmButtonText:
        '<div ><i class="fa fa-check" ></i> Aceptar</div>',

    })
  }
  msgConfirmSaveWithButtons2(title: string, icon: any, lines: any) {
    let html: string = "";
    console.log(lines);
    if (lines) {
      for (let items of lines) {
        html = html + '<div class="form-group row">' +

          '<div class="col-sm-12" style="padding-top: 20px;">' +
          '    <div class="row justify-content-center">';
        '<ng-container>'
        for (let index = 0; index < items.columns.length; index++) {
          const element = items.columns[index];
          console.log(element);

          html = html + '<div class="col-lg-6  col-sm-6 col-xs-5" style="margin-left: 50x;">' +
            '            <a id="' + element.id + '" onclick = ' + items.action() + ' class="btn btn-default col-12">' +
            '                <i class="' + element.icon + '"></i> ' + element.value +
            '            </a>' +
            '        </div>';
        }

        // '        <div class="col-lg-6  col-sm-6 col-xs-5" style="margin-left: 50x;">'+
        // '            <a id="ticket" onclick = '+items.action.name()+' class="btn btn-default col-12">'+
        // '                <i class="fa fa-arrow-circle-left"></i> Ticket'+
        // '            </a>'+
        // '        </div>'+
        // '        <div class="col-lg-6 col-sm-6 col-xs-5" style="margin-left: 20x;">'+
        // '            <a id="a4" class="btn btn-primary col-12">'+
        // '                <i class="fa fa-arrow-circle-left"></i> A4'+
        // '            </a>'+
        // '        </div>'+
        html = html + '    </div>' +
          '</div>' +
          '</div>';
      }
    }
    lines.forEach((element: any) => {
      console.log(element);
      element.columns.forEach((column: any) => {
        $(document).on('click', '#' + column.id, function () {
          column.action.name();
        });
      });
    });

    return Swal.fire({
      title: title,
      icon: icon,
      html: '<div class="table-responsive">' +
        '<div class="col-sm-12" style="padding-top: 6px;">' +
        html +
        '</div>' +
        '</div>',
      showCloseButton: true,
      allowOutsideClick: false,
      showConfirmButton: true,

      confirmButtonText:
        '<div ><i class="fa fa-check" ></i> Aceptar</div>',

    })

  }
  msgOkSaveWithResult(message: string) {
    Swal.fire('Registro', message, 'success');
  }
  msgOkGenReport() {
    Swal.fire('Registro', `Reporte en proceso de generación`, 'success');
  }
  msgOkGenProcess() {
    Swal.fire('Registro', `Proceso de Regeneración de movimientos en proceso de generación`, 'success');
  }
  msgProblemDate() {
    Swal.fire({
      title: 'Problema al grabar',
      text: `Datos de tipo fecha tienen formato incorrecto`,
      icon: 'warning'
    });
  }
  msgOkDelete() {
    Swal.fire('Eliminación de registro:', `Eliminado con éxito`, 'success');
  }
  msgOkOperation() {
    Swal.fire('Operación realizada correctamente:', `Realizado con éxito`, 'success');
  }
  msgOkInactive() {
    Swal.fire('Inactivación de registro:', `Inactivado con éxito`, 'success');
  }
  msgAccessDenied() {
    Swal.fire('Acceso denegado', `Ud. no tiene acceso a esta acción`, 'error');
  }
  msgProblemNoItems() {
    Swal.fire('Problema para continuar', `No ha agregado items para guardar`, 'error');
  }
  msgProblemItemsCero() {
    Swal.fire('Problema para continuar', `Todos los items deben tener un monto a cancelar`, 'error');
  }
  msgAccessDeniedWithMessage(msg: string) {
    Swal.fire("Acceso Denegado", msg, 'error');
  }
  msgAccessDeniedWithParams(title: string, msg: string) {
    Swal.fire(title, msg, 'error');
  }
  msgHTTP400WithMessage(msg: string) {
    Swal.fire("Error en proceso", msg, 'warning');
  }
  msgWarning(title: string, msg: string) {
    Swal.fire(title, msg, 'warning');
  }
  msgProblemDelete() {
    Swal.fire({
      title: 'Problema al eliminar',
      text: `Existe información que hace referencia a este registro`,
      icon: 'error'
    });
  }
  isEmail(search: string): boolean {
    var serchfind: boolean;

    var regexp = new RegExp(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/);

    serchfind = regexp.test(search);

    console.log(serchfind)
    return serchfind
  }
  getTranslate(header: any) {
    return this.translate.instant(header);
  }
  monthDiff(d1, d2) { 
    var months; 
    months = (d2.getFullYear() - d1.getFullYear()) * 12; 
    months = months + d1.getMonth() - d2.getMonth();
    return months <= 0 ? 0 : months;
  }
  dayDiff(date1, date2) { 
    var diff = Math.abs(date1.getTime() - date2.getTime());
    var diffDays = Math.ceil(diff / (1000 * 3600 * 24)); 
    return diffDays;
  }
  getFirstDayOfMonth(){
    var date = new Date();
    return new Date(date.getFullYear(), date.getMonth(), 1);
  }
  getLastDayOfMonth(){
    var date = new Date();
    return new Date(date.getFullYear(), date.getMonth() + 1, 0);
  }

}
