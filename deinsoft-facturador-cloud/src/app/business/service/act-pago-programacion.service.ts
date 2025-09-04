import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { ActPagoProgramacion } from '../model/act-pago-programacion.model';

@Injectable({
  providedIn: 'root'
})
export class ActPagoProgramacionService {
  url:string  = environment.apiUrl + '/api/business/act-pago-programacion';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:ActPagoProgramacion):Observable<any>{
    let params = new HttpParams();    return this.http.get<ActPagoProgramacion[]>(`${this.url}/get-all-act-pago-programacion`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<ActPagoProgramacion[]>(`${this.url}/get-all-act-pago-programacion-combo`);
  }
  public getAllByActComprobanteId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<ActPagoProgramacion[]>(`${this.url}/get-all-act-pago-programacion-by-act-comprobante`,{params});
  } 
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<ActPagoProgramacion>(`${this.url}/get-act-pago-programacion`,{params});
  }
  public save(form:any): Observable<ActPagoProgramacion>{
    return this.http.post<ActPagoProgramacion>(this.url+'/save-act-pago-programacion',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-act-pago-programacion', { observe: 'response' ,params}); 
  }
  public getAllByCnfMaestroId(id:number,fecha:any,cnfLocalId:any, onlyPendientes: any):Observable<any>{
    let params = new HttpParams()
    .set("id",id?.toString())
    .set("fechaVencimiento",fecha)
    .set("cnfLocalId",cnfLocalId)
    .set("onlyPendientes",onlyPendientes);
    return this.http.get<ActPagoProgramacion[]>(`${this.url}/get-all-act-pago-programacion-by-cnf-maestro`,{params});
  } 
  public getAllCompraByCnfMaestroId(id:number,fecha:any):Observable<any>{
    let params = new HttpParams().set("id",id.toString())
    .set("fechaVencimiento",fecha);
    return this.http.get<ActPagoProgramacion[]>(`${this.url}/get-all-act-pago-programacion-compra-by-cnf-maestro`,{params});
  } 
  
}

