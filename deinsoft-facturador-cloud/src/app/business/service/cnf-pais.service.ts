import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { CnfPais } from '../model/cnf-pais.model';

@Injectable({
  providedIn: 'root'
})
export class CnfPaisService {
  url:string  = environment.apiUrl + '/api/business/cnf-pais';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:CnfPais):Observable<any>{
    let params = new HttpParams().set("nombre",arg1.nombre)
.set("isocode",arg1.isocode)
;    return this.http.get<CnfPais[]>(`${this.url}/get-all-cnf-pais`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<CnfPais[]>(`${this.url}/get-all-cnf-pais-combo`);
  }
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<CnfPais>(`${this.url}/get-cnf-pais`,{params});
  }
  public save(form:any): Observable<CnfPais>{
    return this.http.post<CnfPais>(this.url+'/save-cnf-pais',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-cnf-pais', { observe: 'response' ,params}); 
  }
}

