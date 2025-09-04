import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { CnfEmpresa } from '../model/cnf-empresa.model';

@Injectable({
  providedIn: 'root'
})
export class CnfEmpresaService {
  url:string  = environment.apiUrl + '/api/business/cnf-empresa';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:CnfEmpresa):Observable<any>{
    let params = new HttpParams().set("nombre",arg1.nombre)
.set("descripcion",arg1.descripcion)
.set("nroDocumento",arg1.nroDocumento)
.set("direccion",arg1.direccion)
.set("telefono",arg1.telefono)
.set("estado",arg1.estado)
.set("token",arg1.token)
;    return this.http.get<CnfEmpresa[]>(`${this.url}/get-all-cnf-empresa`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<CnfEmpresa[]>(`${this.url}/get-all-cnf-empresa-combo`);
  }
  public getAllByCnfTipoDocumentoId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<CnfEmpresa[]>(`${this.url}/get-all-cnf-empresa-by-cnf-tipo-documento`,{params});
  } 
  public getAllByCnfDistritoId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<CnfEmpresa[]>(`${this.url}/get-all-cnf-empresa-by-cnf-distrito`,{params});
  } 
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<CnfEmpresa>(`${this.url}/get-cnf-empresa`,{params});
  }
  public save(form:any): Observable<CnfEmpresa>{
    return this.http.post<CnfEmpresa>(this.url+'/save-cnf-empresa',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-cnf-empresa', { observe: 'response' ,params}); 
  }
}

