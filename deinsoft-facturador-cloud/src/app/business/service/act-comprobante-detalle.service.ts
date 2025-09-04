import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { ActComprobanteDetalle } from '../model/act-comprobante-detalle.model';

@Injectable({
  providedIn: 'root'
})
export class ActComprobanteDetalleService {
  url:string  = environment.apiUrl + '/api/business/act-comprobante-detalle';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:ActComprobanteDetalle):Observable<any>{
    let params = new HttpParams().set("descripcion",arg1.descripcion)
;    return this.http.get<ActComprobanteDetalle[]>(`${this.url}/get-all-act-comprobante-detalle`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<ActComprobanteDetalle[]>(`${this.url}/get-all-act-comprobante-detalle-combo`);
  }
  public getAllByActComprobanteId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<ActComprobanteDetalle[]>(`${this.url}/get-all-act-comprobante-detalle-by-act-comprobante`,{params});
  } 
  public getAllByCnfProductoId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<ActComprobanteDetalle[]>(`${this.url}/get-all-act-comprobante-detalle-by-cnf-producto`,{params});
  } 
  public getAllByCnfImpuestoCondicionId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<ActComprobanteDetalle[]>(`${this.url}/get-all-act-comprobante-detalle-by-cnf-impuesto-condicion`,{params});
  } 
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<ActComprobanteDetalle>(`${this.url}/get-act-comprobante-detalle`,{params});
  }
  public save(form:any): Observable<ActComprobanteDetalle>{
    return this.http.post<ActComprobanteDetalle>(this.url+'/save-act-comprobante-detalle',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-act-comprobante-detalle', { observe: 'response' ,params}); 
  }
}

