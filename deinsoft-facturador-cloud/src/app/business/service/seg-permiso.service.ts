import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { SegPermiso } from '../model/seg-permiso.model';
@Injectable({
  providedIn: 'root'
})
export class SegPermisoService {
  url:string  = environment.apiUrl + '/api/business/seg-permiso';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:SegPermiso):Observable<any>{
    let params = new HttpParams();    return this.http.get<SegPermiso[]>(`${this.url}/get-all-seg-permiso`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<SegPermiso[]>(`${this.url}/get-all-seg-permiso-combo`);
  }
  public getAllBySegRolId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<SegPermiso[]>(`${this.url}/get-all-seg-permiso-by-seg-rol`,{params});
  } 
  public getAllBySegMenuId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<SegPermiso[]>(`${this.url}/get-all-seg-permiso-by-seg-menu`,{params});
  } 
  public getAllBySegAccionId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<SegPermiso[]>(`${this.url}/get-all-seg-permiso-by-seg-accion`,{params});
  } 
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<SegPermiso>(`${this.url}/get-seg-permiso`,{params});
  }
  public save(form:any): Observable<SegPermiso>{
    return this.http.post<SegPermiso>(this.url+'/save-seg-permiso',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-seg-permiso', { observe: 'response' ,params}); 
  }
  public getAllBySegRolNombre(nombre:any): Promise<any>{
    let params = new HttpParams().set("nombre",nombre.toString());
    return this.http.get<SegPermiso[]>(`${this.url}/get-all-seg-permiso-by-seg-rol-name`,{params}).toPromise();;
  }
}

