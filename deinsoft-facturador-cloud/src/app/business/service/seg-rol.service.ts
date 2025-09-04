import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { SegRol } from '../model/seg-rol.model';

@Injectable({
  providedIn: 'root'
})
export class SegRolService {
  url:string  = environment.apiUrl + '/api/business/seg-rol';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:SegRol):Observable<any>{
    let params = new HttpParams().set("nombre",arg1.nombre)
;    return this.http.get<SegRol[]>(`${this.url}/get-all-seg-rol`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<SegRol[]>(`${this.url}/get-all-seg-rol-combo`);
  }
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<SegRol>(`${this.url}/get-seg-rol`,{params});
  }
  public save(form:any): Observable<SegRol>{
    return this.http.post<SegRol>(this.url+'/save-seg-rol',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-seg-rol', { observe: 'response' ,params}); 
  }
}

