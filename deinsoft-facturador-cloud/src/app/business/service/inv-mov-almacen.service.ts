import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { InvMovAlmacen } from '../model/inv-mov-almacen.model';

@Injectable({
  providedIn: 'root'
})
export class InvMovAlmacenService {
  url:string  = environment.apiUrl + '/api/business/inv-mov-almacen';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:InvMovAlmacen):Observable<any>{
    let params = new HttpParams().set("serie",arg1.serie)
        .set("numero",arg1.numero)
        .set("numeroRef",arg1.numeroRef)
        .set("observacion",arg1.observacion)
        .set("flagEstado",arg1.flagEstado);    
    return this.http.get<InvMovAlmacen[]>(`${this.url}/get-all-inv-mov-almacen`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<InvMovAlmacen[]>(`${this.url}/get-all-inv-mov-almacen-combo`);
  }
  public getAllByInvTipoMovAlmacenId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<InvMovAlmacen[]>(`${this.url}/get-all-inv-mov-almacen-by-inv-tipo-mov-almacen`,{params});
  } 
  public getAllByCnfMaestroId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<InvMovAlmacen[]>(`${this.url}/get-all-inv-mov-almacen-by-cnf-maestro`,{params});
  } 
  public getAllByCnfLocalId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<InvMovAlmacen[]>(`${this.url}/get-all-inv-mov-almacen-by-cnf-local`,{params});
  } 
  public getAllByCnfTipoComprobanteId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<InvMovAlmacen[]>(`${this.url}/get-all-inv-mov-almacen-by-cnf-tipo-comprobante`,{params});
  } 
  public getAllByInvAlmacenId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<InvMovAlmacen[]>(`${this.url}/get-all-inv-mov-almacen-by-inv-almacen`,{params});
  } 
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<InvMovAlmacen>(`${this.url}/get-inv-mov-almacen`,{params});
  }
  public save(form:any): Observable<InvMovAlmacen>{
    return this.http.post<InvMovAlmacen>(this.url+'/save-inv-mov-almacen',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-inv-mov-almacen', { observe: 'response' ,params}); 
  }
  public getAllData2(arg1:any):Observable<any>{
    return this.http.get<InvMovAlmacen[]>(`${this.url}/get-all-inv-mov-almacen`,arg1);
  }
}

