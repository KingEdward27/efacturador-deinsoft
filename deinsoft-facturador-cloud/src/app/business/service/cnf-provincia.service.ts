import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { CnfProvincia } from '../model/cnf-provincia.model';

@Injectable({
  providedIn: 'root'
})
export class CnfProvinciaService {
  url:string  = environment.apiUrl + '/api/business/cnf-provincia';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:CnfProvincia):Observable<any>{
    let params = new HttpParams().set("nombre",arg1.nombre)
;    return this.http.get<CnfProvincia[]>(`${this.url}/get-all-cnf-provincia`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<CnfProvincia[]>(`${this.url}/get-all-cnf-provincia-combo`);
  }
  public getAllByCnfRegionId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<CnfProvincia[]>(`${this.url}/get-all-cnf-provincia-by-cnf-region`,{params});
  } 
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<CnfProvincia>(`${this.url}/get-cnf-provincia`,{params});
  }
  public save(form:any): Observable<CnfProvincia>{
    return this.http.post<CnfProvincia>(this.url+'/save-cnf-provincia',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-cnf-provincia', { observe: 'response' ,params}); 
  }
}

