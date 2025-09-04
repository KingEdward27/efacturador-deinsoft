import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { CnfDistrito } from '../model/cnf-distrito.model';

@Injectable({
  providedIn: 'root'
})
export class CnfDistritoService {
  url:string  = environment.apiUrl + '/api/business/cnf-distrito';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:CnfDistrito):Observable<any>{
    let params = new HttpParams().set("nombre",arg1.nombre)
;    return this.http.get<CnfDistrito[]>(`${this.url}/get-all-cnf-distrito`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<CnfDistrito[]>(`${this.url}/get-all-cnf-distrito-combo`);
  }
  public getAllByCnfProvinciaId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<CnfDistrito[]>(`${this.url}/get-all-cnf-distrito-by-cnf-provincia`,{params});
  } 
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<CnfDistrito>(`${this.url}/get-cnf-distrito`,{params});
  }
  public save(form:any): Observable<CnfDistrito>{
    return this.http.post<CnfDistrito>(this.url+'/save-cnf-distrito',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-cnf-distrito', { observe: 'response' ,params}); 
  }
}

