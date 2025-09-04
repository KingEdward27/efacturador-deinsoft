import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { CnfTipoDocumento } from '../model/cnf-tipo-documento.model';

@Injectable({
  providedIn: 'root'
})
export class CnfTipoDocumentoService {
  url: string = environment.apiUrl + '/api/business/cnf-tipo-documento';
  constructor(private http: HttpClient,
    private router: Router) {
  }

  public getAllData(arg1: CnfTipoDocumento): Observable<any> {
    let params = new HttpParams().set("abreviatura", arg1.abreviatura)
      .set("nombre", arg1.nombre)
      .set("codigoSunat", arg1.codigoSunat)
      .set("flagEstado", arg1.flagEstado)
      ; return this.http.get<CnfTipoDocumento[]>(`${this.url}/get-all-cnf-tipo-documento`, { params });
  }
  public getAllDataCombo(): Observable<any> {
    return this.http.get<CnfTipoDocumento[]>(`${this.url}/get-all-cnf-tipo-documento-combo`);
  }
  public getData(arg1: string): Observable<any> {
    let params = new HttpParams().set("id", arg1)
    return this.http.get<CnfTipoDocumento>(`${this.url}/get-cnf-tipo-documento`, { params });
  }
  public save(form: any): Observable<CnfTipoDocumento> {
    return this.http.post<CnfTipoDocumento>(this.url + '/save-cnf-tipo-documento', form);
  }
  public delete(arg1: string): Observable<HttpResponse<{}>> {
    let params = new HttpParams().set("id", arg1);
    return this.http.delete(this.url + '/delete-cnf-tipo-documento', { observe: 'response', params });
  }
}

