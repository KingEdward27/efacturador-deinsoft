import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { ActCajaOperacion } from '../model/act-caja-operacion.model';
@Injectable({
  providedIn: 'root'
})
export class ActCajaOperacionService {
  url:string  = environment.apiUrl + '/api/business/act-caja-operacion';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:ActCajaOperacion):Observable<any>{
    let params = new HttpParams().set("flagIngreso",arg1.flagIngreso)
.set("estado",arg1.estado)
;    return this.http.get<ActCajaOperacion[]>(`${this.url}/get-all-act-caja-operacion`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<ActCajaOperacion[]>(`${this.url}/get-all-act-caja-operacion-combo`);
  }
  public getAllByActCajaTurnoId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<ActCajaOperacion[]>(`${this.url}/get-all-act-caja-operacion-by-act-caja-turno`,{params});
  } 
  public getAllByActComprobanteId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<ActCajaOperacion[]>(`${this.url}/get-all-act-caja-operacion-by-act-comprobante`,{params});
  } 
  public getAllByActPagoId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<ActCajaOperacion[]>(`${this.url}/get-all-act-caja-operacion-by-act-pago`,{params});
  } 
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<ActCajaOperacion>(`${this.url}/get-act-caja-operacion`,{params});
  }
  public save(form:any): Observable<ActCajaOperacion>{
    return this.http.post<ActCajaOperacion>(this.url+'/save-act-caja-operacion',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-act-caja-operacion', { observe: 'response' ,params}); 
  }
  public getReport(form: any): Observable<ActCajaOperacion> {
    return this.http.post<ActCajaOperacion>(this.url + '/get-report-act-caja-operacion', form);
  }
}

