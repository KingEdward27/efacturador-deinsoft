import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { CnfTipoComprobante } from '../model/cnf-tipo-comprobante.model';

@Injectable({
  providedIn: 'root'
})
export class CnfTipoComprobanteService {
  url:string  = environment.apiUrl + '/api/business/cnf-tipo-comprobante';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:CnfTipoComprobante):Observable<any>{
    let params = new HttpParams().set("nombre",arg1.nombre)
.set("codigoSunat",arg1.codigoSunat)
.set("codigo",arg1.codigo)
.set("flagElectronico",arg1.flagElectronico)
;    return this.http.get<CnfTipoComprobante[]>(`${this.url}/get-all-cnf-tipo-comprobante`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<CnfTipoComprobante[]>(`${this.url}/get-all-cnf-tipo-comprobante-combo`);
  }
  public getAllDataComboVentas():Observable<any>{
    return this.http.get<CnfTipoComprobante[]>(`${this.url}/get-all-cnf-tipo-comprobante-combo-ventas`);
  }
  public getAllDataComboMovAlmacen():Observable<any>{
    return this.http.get<CnfTipoComprobante[]>(`${this.url}/get-all-cnf-tipo-comprobante-combo-almacen`);
  }
  public getAllDataComboContrato():Observable<any>{
    return this.http.get<CnfTipoComprobante[]>(`${this.url}/get-all-cnf-tipo-comprobante-combo-contrato`);
  }
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<CnfTipoComprobante>(`${this.url}/get-cnf-tipo-comprobante`,{params});
  }
  public save(form:any): Observable<CnfTipoComprobante>{
    return this.http.post<CnfTipoComprobante>(this.url+'/save-cnf-tipo-comprobante',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-cnf-tipo-comprobante', { observe: 'response' ,params}); 
  }
}

