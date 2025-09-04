import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { InvTipoMovAlmacen } from '../model/inv-tipo-mov-almacen.model';

@Injectable({
  providedIn: 'root'
})
export class InvTipoMovAlmacenService {
  url:string  = environment.apiUrl + '/api/business/inv-tipo-mov-almacen';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:InvTipoMovAlmacen):Observable<any>{
    let params = new HttpParams().set("nombre",arg1.nombre)
.set("codigoSunat",arg1.codigoSunat)
.set("naturaleza",arg1.naturaleza)
;    return this.http.get<InvTipoMovAlmacen[]>(`${this.url}/get-all-inv-tipo-mov-almacen`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<InvTipoMovAlmacen[]>(`${this.url}/get-all-inv-tipo-mov-almacen-combo`);
  }
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<InvTipoMovAlmacen>(`${this.url}/get-inv-tipo-mov-almacen`,{params});
  }
  public save(form:any): Observable<InvTipoMovAlmacen>{
    return this.http.post<InvTipoMovAlmacen>(this.url+'/save-inv-tipo-mov-almacen',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-inv-tipo-mov-almacen', { observe: 'response' ,params}); 
  }
}

