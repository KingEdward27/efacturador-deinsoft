import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { CnfMoneda } from '../model/cnf-moneda.model';

@Injectable({
  providedIn: 'root'
})
export class CnfMonedaService {
  url:string  = environment.apiUrl + '/api/business/cnf-moneda';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:CnfMoneda):Observable<any>{
    let params = new HttpParams().set("codigo",arg1.codigo)
.set("nombre",arg1.nombre)
.set("flagEstado",arg1.flagEstado)
;    return this.http.get<CnfMoneda[]>(`${this.url}/get-all-cnf-moneda`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<CnfMoneda[]>(`${this.url}/get-all-cnf-moneda-combo`);
  }
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<CnfMoneda>(`${this.url}/get-cnf-moneda`,{params});
  }
  public save(form:any): Observable<CnfMoneda>{
    return this.http.post<CnfMoneda>(this.url+'/save-cnf-moneda',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-cnf-moneda', { observe: 'response' ,params}); 
  }
}

