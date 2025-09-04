import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { SegUsuario } from '../model/seg-usuario.model';
import { CnfEmpresa } from '../model/cnf-empresa.model';

@Injectable({
  providedIn: 'root'
})
export class SegUsuarioService {
  url:string  = environment.apiUrl + '/api/v1/users';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getListEmpresaByUser(id:number):Observable<any>{
    let params = new HttpParams().set("id",id)
;    return this.http.get<any[]>(`${this.url}/get-empresa-by-user`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<SegUsuario[]>(`${this.url}/get-all-seg-usuario-combo`);
  }
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<SegUsuario>(`${this.url}/get-seg-usuario`,{params});
  }
  public save(form:any): Observable<SegUsuario>{
    return this.http.post<SegUsuario>(this.url+'/save-seg-usuario',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-seg-usuario', { observe: 'response' ,params}); 
  }
}

