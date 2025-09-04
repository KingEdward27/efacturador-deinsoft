import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { ActCajaTurno } from '../model/act-caja-turno.model';

@Injectable({
  providedIn: 'root'
})
export class ActCajaTurnoService {
  url:string  = environment.apiUrl + '/api/business/act-caja-turno';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:ActCajaTurno):Observable<any>{
    let params = new HttpParams().set("estado",arg1.estado)
;    return this.http.get<ActCajaTurno[]>(`${this.url}/get-all-act-caja-turno`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<ActCajaTurno[]>(`${this.url}/get-all-act-caja-turno-combo`);
  }
  public getAllBySegUsuarioId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<ActCajaTurno[]>(`${this.url}/get-all-act-caja-turno-by-seg-usuario`,{params});
  } 
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<ActCajaTurno>(`${this.url}/get-act-caja-turno`,{params});
  }
  public save(form:any): Observable<ActCajaTurno>{
    return this.http.post<ActCajaTurno>(this.url+'/save-act-caja-turno',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-act-caja-turno', { observe: 'response' ,params}); 
  }
  public getReport(form: any): Observable<ActCajaTurno> {
    return this.http.post<ActCajaTurno>(this.url + '/get-report-act-caja-turno', form);
  }
}

