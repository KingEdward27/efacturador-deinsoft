import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { CnfCategoria } from '../model/cnf-categoria.model';

@Injectable({
  providedIn: 'root'
})
export class CnfCategoriaService {
  url:string  = environment.apiUrl + '/api/business/cnf-categoria';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:CnfCategoria):Observable<any>{
    let params = new HttpParams().set("nombre",arg1.nombre)
.set("flagEstado",arg1.flagEstado)
;    return this.http.get<CnfCategoria[]>(`${this.url}/get-all-cnf-categoria`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<CnfCategoria[]>(`${this.url}/get-all-cnf-categoria-combo`);
  }
  public getAllByCnfEmpresaId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<CnfCategoria[]>(`${this.url}/get-all-cnf-categoria-by-cnf-empresa`,{params});
  } 
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<CnfCategoria>(`${this.url}/get-cnf-categoria`,{params});
  }
  public save(form:any): Observable<CnfCategoria>{
    return this.http.post<CnfCategoria>(this.url+'/save-cnf-categoria',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-cnf-categoria', { observe: 'response' ,params}); 
  }
}

