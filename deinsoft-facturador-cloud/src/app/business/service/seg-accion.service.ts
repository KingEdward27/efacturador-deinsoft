import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { SegAccion } from '../../business/model/seg-accion.model';

@Injectable({
  providedIn: 'root'
})
export class SegAccionService {
  url:string  = environment.apiUrl + '/api/business/seg-accion';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:SegAccion):Observable<any>{
    let params = new HttpParams().set("nombre",arg1.nombre)
.set("descripcion",arg1.descripcion)
;    return this.http.get<SegAccion[]>(`${this.url}/get-all-seg-accion`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<SegAccion[]>(`${this.url}/get-all-seg-accion-combo`);
  }
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<SegAccion>(`${this.url}/get-seg-accion`,{params});
  }
  public save(form:any): Observable<SegAccion>{
    return this.http.post<SegAccion>(this.url+'/save-seg-accion',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-seg-accion', { observe: 'response' ,params}); 
  }
}

