import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { CnfSubCategoria } from '../model/cnf-sub-categoria.model';

@Injectable({
  providedIn: 'root'
})
export class CnfSubCategoriaService {
  url:string  = environment.apiUrl + '/api/business/cnf-sub-categoria';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:CnfSubCategoria):Observable<any>{
    let params = new HttpParams().set("nombre",arg1.nombre)
.set("flagEstado",arg1.flagEstado)
;    return this.http.get<CnfSubCategoria[]>(`${this.url}/get-all-cnf-sub-categoria`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<CnfSubCategoria[]>(`${this.url}/get-all-cnf-sub-categoria-combo`);
  }
  public getAllByCnfCategoriaId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<CnfSubCategoria[]>(`${this.url}/get-all-cnf-sub-categoria-by-cnf-categoria`,{params});
  } 
  public getAllByCnfEmpresaId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<CnfSubCategoria[]>(`${this.url}/get-all-cnf-sub-categoria-by-cnf-empresa`,{params});
  } 
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<CnfSubCategoria>(`${this.url}/get-cnf-sub-categoria`,{params});
  }
  public save(form:any): Observable<CnfSubCategoria>{
    return this.http.post<CnfSubCategoria>(this.url+'/save-cnf-sub-categoria',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-cnf-sub-categoria', { observe: 'response' ,params}); 
  }
}

