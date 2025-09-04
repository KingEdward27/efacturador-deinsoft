import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { CnfFormaPago } from '../model/cnf-forma-pago.model';

@Injectable({
  providedIn: 'root'
})
export class CnfFormaPagoService {
  url:string  = environment.apiUrl + '/api/business/cnf-forma-pago';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:CnfFormaPago):Observable<any>{
    let params = new HttpParams().set("nombre",arg1.nombre)
.set("flagEstado",arg1.flagEstado)
;    return this.http.get<CnfFormaPago[]>(`${this.url}/get-all-cnf-forma-pago`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<CnfFormaPago[]>(`${this.url}/get-all-cnf-forma-pago-combo`);
  }
  public getAllByCnfEmpresaId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<CnfFormaPago[]>(`${this.url}/get-all-cnf-forma-pago-by-cnf-empresa`,{params});
  } 
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<CnfFormaPago>(`${this.url}/get-cnf-forma-pago`,{params});
  }
  public save(form:any): Observable<CnfFormaPago>{
    return this.http.post<CnfFormaPago>(this.url+'/save-cnf-forma-pago',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-cnf-forma-pago', { observe: 'response' ,params}); 
  }
}

