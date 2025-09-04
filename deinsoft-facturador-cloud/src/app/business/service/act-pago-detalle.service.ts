import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { ActPagoDetalle } from '../model/act-pago-detalle.model';

@Injectable({
  providedIn: 'root'
})
export class ActPagoDetalleService {
  url:string  = environment.apiUrl;
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:ActPagoDetalle):Observable<any>{
    let params = new HttpParams();    return this.http.get<ActPagoDetalle[]>(`${this.url}/get-all-act-pago-detalle`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<ActPagoDetalle[]>(`${this.url}/get-all-act-pago-detalle-combo`);
  }
  public getAllByActPagoId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<ActPagoDetalle[]>(`${this.url}/get-all-act-pago-detalle-by-act-pago`,{params});
  } 
  public getAllByActPagoProgramacionId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<ActPagoDetalle[]>(`${this.url}/get-all-act-pago-detalle-by-act-pago-programacion`,{params});
  } 
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<ActPagoDetalle>(`${this.url}/get-act-pago-detalle`,{params});
  }
  public save(form:any): Observable<ActPagoDetalle>{
    return this.http.post<ActPagoDetalle>(this.url+'/save-act-pago-detalle',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-act-pago-detalle', { observe: 'response' ,params}); 
  }
}

