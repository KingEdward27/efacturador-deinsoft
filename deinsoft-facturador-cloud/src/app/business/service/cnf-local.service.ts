import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { CnfLocal } from '../model/cnf-local.model';

@Injectable({
  providedIn: 'root'
})
export class CnfLocalService {
  url:string  = environment.apiUrl + '/api/business/cnf-local';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:CnfLocal):Observable<any>{
    let params = new HttpParams().set("nombre",arg1.nombre)
.set("direccion",arg1.direccion)
;    return this.http.get<CnfLocal[]>(`${this.url}/get-all-cnf-local`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<CnfLocal[]>(`${this.url}/get-all-cnf-local-combo`);
  }
  public getAllByCnfEmpresaId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<CnfLocal[]>(`${this.url}/get-all-cnf-local-by-cnf-empresa`,{params});
  } 
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<CnfLocal>(`${this.url}/get-cnf-local`,{params});
  }
  public save(form:any): Observable<CnfLocal>{
    return this.http.post<CnfLocal>(this.url+'/save-cnf-local',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-cnf-local', { observe: 'response' ,params}); 
  }
}

