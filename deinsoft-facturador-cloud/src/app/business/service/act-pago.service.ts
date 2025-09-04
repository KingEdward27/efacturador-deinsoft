import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { ActPago } from '../model/act-pago.model';

@Injectable({
  providedIn: 'root'
})
export class ActPagoService {
  url:string  = environment.apiUrl + '/api/business/act-pago';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:ActPago):Observable<any>{
    let params = new HttpParams()
    .set("serie",arg1.serie.toString())
    .set("numero",arg1.numero.toString());    
    return this.http.get<ActPago[]>(`${this.url}/get-all-act-pago`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<ActPago[]>(`${this.url}/get-all-act-pago-combo`);
  }
  public getAllByActPagoProgramacionId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<ActPago[]>(`${this.url}/get-all-act-pago-by-act-pago-programacion`,{params});
  } 
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<ActPago>(`${this.url}/get-act-pago`,{params});
  }
  public save(form:any): Observable<ActPago>{
    return this.http.post<ActPago>(this.url+'/save-act-pago',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-act-pago', { observe: 'response' ,params}); 
  }
  public saveFromList(form:any): Observable<any>{
    return this.http.post<any>(this.url+'/save-list-act-pago',form); 
  }
  public getReport(form: any): Observable<ActPago> {
    return this.http.post<ActPago>(this.url + '/get-report-act-pago', form);
  }
}

