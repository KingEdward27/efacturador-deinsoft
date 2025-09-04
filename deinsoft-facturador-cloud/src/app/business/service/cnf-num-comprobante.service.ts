import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { CnfNumComprobante } from '../model/cnf-num-comprobante.model';

@Injectable({
  providedIn: 'root'
})
export class CnfNumComprobanteService {
  url:string  = environment.apiUrl + '/api/business/cnf-num-comprobante';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:CnfNumComprobante):Observable<any>{
    let params = new HttpParams().set("serie",arg1.serie)
;    return this.http.get<CnfNumComprobante[]>(`${this.url}/get-all-cnf-num-comprobante`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<CnfNumComprobante[]>(`${this.url}/get-all-cnf-num-comprobante-combo`);
  }
  public getAllByCnfTipoComprobanteId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<CnfNumComprobante[]>(`${this.url}/get-all-cnf-num-comprobante-by-cnf-tipo-comprobante`,{params});
  } 
  public getAllByCnfLocalId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<CnfNumComprobante[]>(`${this.url}/get-all-cnf-num-comprobante-by-cnf-local`,{params});
  } 
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<CnfNumComprobante>(`${this.url}/get-cnf-num-comprobante`,{params});
  }
  public getDataByCnfTipoComprobanteIdAndCnfLocalId(arg1:string,idLocal:string):Observable<any>{
    let params = new HttpParams().set("id",arg1).set("idLocal",idLocal)
    return this.http.get<CnfNumComprobante>(`${this.url}/get-cnf-num-comprobante-by-cnf-tipo-comprobante-and-cnf-local`,{params});
  }
  public save(form:any): Observable<CnfNumComprobante>{
    return this.http.post<CnfNumComprobante>(this.url+'/save-cnf-num-comprobante',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-cnf-num-comprobante', { observe: 'response' ,params}); 
  }
}

