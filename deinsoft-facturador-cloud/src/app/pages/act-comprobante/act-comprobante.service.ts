import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { ActComprobante } from './act-comprobante.model';

@Injectable({
  providedIn: 'root'
})
export class ActComprobanteService {
  url: string = environment.apiUrl + '/backend';
  constructor(private http: HttpClient,
    private router: Router) {
  }

  public getAllData(arg1: ActComprobante): Observable<any> {
    let params = new HttpParams().set("serie", arg1.serie)
      .set("numero", arg1.numero)
      .set("observacion", arg1.observacion)
      .set("flagEstado", arg1.flagEstado)
      .set("flagIsventa", arg1.flagIsventa)
      .set("envioPseFlag", arg1.envioPseFlag)
      .set("envioPseMensaje", arg1.envioPseMensaje)
      .set("xmlhash", arg1.xmlhash)
      .set("codigoqr", arg1.codigoqr)
      .set("numTicket", arg1.numTicket)
      ; return this.http.get<ActComprobante[]>(`${this.url}/get-all-act-comprobante`, { params });
  }
  public resumenDiario(ids: any): Observable<any> {
    return this.http.post<any>(this.url + '/resumendiario?ids=' + ids.toString(), null);
  }
  public genXml(id: any): Observable<any> {
    return this.http.post<any>(this.url + '/xml?id=' + id.toString(), null);
  }
  public genNotaCredito(serie: any, numero: any, empresaId: any): Observable<any> {
    return this.http.post<any>(this.url
      + '/gen-xml-notacredito-by-numdoc?serie=' + serie
      + '&numero=' + numero
      + '&empresaId=' + empresaId, null);
  }

  public sendApi(id: any): Observable<any> {
    console.log(id);

    return this.http.post<any>(this.url + '/sunat?id=' + id.toString(), null);
  }

  public getReport(form: any): Observable<any> {
    return this.http.post<any>(this.url + '/listar', form);
  }
  public getListEmpresaByUser(id: number): Observable<any> {
    let params = new HttpParams().set("id", id);
    return this.http.get<any[]>(`${this.url}/get-empresa-by-user`, { params });
  }

  public getListPeriodos(idEmpresa: number,libro: string): Observable<any> {
    let params = new HttpParams().set("idEmpresa", idEmpresa).set("libro", libro);
    return this.http.get<any[]>(`${this.url}/get-periodos`, { params });
  }

  public getListPeriodosDetail(idEmpresa: number, anio: string,libro: string): Observable<any> {
    console.log(anio);

    let params = new HttpParams()
      .set("idEmpresa", idEmpresa)
      .set("anio", anio)
      .set("libro", libro);
    return this.http.get<any[]>(`${this.url}/get-periodos-detail`, { params });
  }

  // public getResumenRcle(empresaId: any, fechaDesde: any, fechaHasta: any): Observable<any> {
  //   return this.http.post<any>(this.url
  //     + '/get-resumen-rlie?empresaId=' + empresaId
  //     + '&fechaDesde=' + fechaDesde
  //     + '&fechaHasta=' + fechaHasta, null);
  // }

  public getResumenRcle(form: any): Observable<any> {
    return this.http.post<any>(this.url + '/get-resumen-rlie', form);
  }

  public getPropuestaRcle(form: any): Observable<any> {
    return this.http.post<any>(this.url + '/get-propuesta-rlie', form);
  }

  public getComparacionRcle(form: any): Observable<any> {
    return this.http.post<any>(this.url + '/get-list-comparacion-rlie', form);
  }

  public validateApi(id: any): Observable<any> {
    console.log(id);

    return this.http.post<any>(this.url + '/validate?id=' + id.toString(), null);
  }
  
}

