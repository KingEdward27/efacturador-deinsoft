import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { ActContratoCorte } from '../model/act-contrato-corte.model';

@Injectable({
  providedIn: 'root'
})
export class ActContratoCorteService {
  url:string  = environment.apiUrl + '/api/business/act-contrato-corte';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:ActContratoCorte):Observable<any>{
    let params = new HttpParams().set("flgEstado",arg1.flgEstado)
;    return this.http.get<ActContratoCorte[]>(`${this.url}/get-all-act-contrato-corte`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<ActContratoCorte[]>(`${this.url}/get-all-act-contrato-corte-combo`);
  }
  public getAllByActContratoId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<ActContratoCorte[]>(`${this.url}/get-all-act-contrato-corte-by-act-contrato`,{params});
  } 
  public getAllBySegUsuarioId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<ActContratoCorte[]>(`${this.url}/get-all-act-contrato-corte-by-seg-usuario`,{params});
  } 
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<ActContratoCorte>(`${this.url}/get-act-contrato-corte`,{params});
  }
  public save(form:any): Observable<ActContratoCorte>{
    return this.http.post<ActContratoCorte>(this.url+'/save-act-contrato-corte',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-act-contrato-corte', { observe: 'response' ,params}); 
  }
}

