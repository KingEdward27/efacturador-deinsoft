import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { CnfZona } from '../model/cnf-zona';

@Injectable({
  providedIn: 'root'
})
export class CnfZonaService {
  url: string = environment.apiUrl + '/api/business/cnf-zona';
  constructor(private http: HttpClient,
    private router: Router) {
  }

  public getAllData(arg1: CnfZona): Observable<any> {
    let params = new HttpParams().set("nombre", arg1.nombre)
      ; return this.http.get<CnfZona[]>(`${this.url}/get-all-cnf-zona`, { params });
  }
  public getAllDataCombo(): Observable<any> {
    return this.http.get<CnfZona[]>(`${this.url}/get-all-cnf-zona-combo`);
  }
  public getAllByCnfEmpresaId(id: number): Observable<any> {
    let params = new HttpParams().set("id", id.toString());
    return this.http.get<CnfZona[]>(`${this.url}/get-all-cnf-zona-by-cnf-empresa`, { params });
  }
  public getData(arg1: string): Observable<any> {
    let params = new HttpParams().set("id", arg1)
    return this.http.get<CnfZona>(`${this.url}/get-cnf-zona`, { params });
  }
  public save(form: any): Observable<CnfZona> {
    return this.http.post<CnfZona>(this.url + '/save-cnf-zona', form);
  }
  public delete(arg1: string): Observable<HttpResponse<{}>> {
    let params = new HttpParams().set("id", arg1);
    return this.http.delete(this.url + '/delete-cnf-zona', { observe: 'response', params });
  }
}

