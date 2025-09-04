import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { CnfMarca } from '../model/cnf-marca.model';

@Injectable({
  providedIn: 'root'
})
export class CnfMarcaService {
  url:string  = environment.apiUrl + '/api/business/cnf-marca';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:CnfMarca):Observable<any>{
    let params = new HttpParams().set("nombre",arg1.nombre)
.set("flagEstado",arg1.flagEstado)
;    return this.http.get<CnfMarca[]>(`${this.url}/get-all-cnf-marca`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<CnfMarca[]>(`${this.url}/get-all-cnf-marca-combo`);
  }
  public getAllByCnfEmpresaId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<CnfMarca[]>(`${this.url}/get-all-cnf-marca-by-cnf-empresa`,{params});
  } 
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<CnfMarca>(`${this.url}/get-cnf-marca`,{params});
  }
  public save(form:any): Observable<CnfMarca>{
    return this.http.post<CnfMarca>(this.url+'/save-cnf-marca',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-cnf-marca', { observe: 'response' ,params}); 
  }
}

