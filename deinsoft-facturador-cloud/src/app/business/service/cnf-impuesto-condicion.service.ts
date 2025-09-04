import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { CnfImpuestoCondicion } from '../model/cnf-impuesto-condicion.model';

@Injectable({
  providedIn: 'root'
})
export class CnfImpuestoCondicionService {
  url:string  = environment.apiUrl + '/api/business/cnf-impuesto-condicion';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:CnfImpuestoCondicion):Observable<any>{
    let params = new HttpParams().set("codigoSunat",arg1.codigoSunat)
.set("nombre",arg1.nombre)
;    return this.http.get<CnfImpuestoCondicion[]>(`${this.url}/get-all-cnf-impuesto-condicion`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<CnfImpuestoCondicion[]>(`${this.url}/get-all-cnf-impuesto-condicion-combo`);
  }
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<CnfImpuestoCondicion>(`${this.url}/get-cnf-impuesto-condicion`,{params});
  }
  public save(form:any): Observable<CnfImpuestoCondicion>{
    return this.http.post<CnfImpuestoCondicion>(this.url+'/save-cnf-impuesto-condicion',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-cnf-impuesto-condicion', { observe: 'response' ,params}); 
  }
}

