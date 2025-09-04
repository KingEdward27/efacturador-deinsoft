import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { CnfUnidadMedida } from '../model/cnf-unidad-medida.model';

@Injectable({
  providedIn: 'root'
})
export class CnfUnidadMedidaService {
  url:string  = environment.apiUrl + '/api/business/cnf-unidad-medida';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:CnfUnidadMedida):Observable<any>{
    let params = new HttpParams().set("codigoSunat",arg1.codigoSunat)
.set("nombre",arg1.nombre)
.set("flagEstado",arg1.flagEstado)
;    return this.http.get<CnfUnidadMedida[]>(`${this.url}/get-all-cnf-unidad-medida`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<CnfUnidadMedida[]>(`${this.url}/get-all-cnf-unidad-medida-combo`);
  }
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<CnfUnidadMedida>(`${this.url}/get-cnf-unidad-medida`,{params});
  }
  public save(form:any): Observable<CnfUnidadMedida>{
    return this.http.post<CnfUnidadMedida>(this.url+'/save-cnf-unidad-medida',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-cnf-unidad-medida', { observe: 'response' ,params}); 
  }
}

