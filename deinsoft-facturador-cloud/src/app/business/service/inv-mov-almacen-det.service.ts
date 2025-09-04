import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { InvMovAlmacenDet } from '../model/inv-mov-almacen-det.model';

@Injectable({
  providedIn: 'root'
})
export class InvMovAlmacenDetService {
  url:string  = environment.apiUrl + '/api/business/inv-mov-almacen-det';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:InvMovAlmacenDet):Observable<any>{
    let params = new HttpParams().set("nroserie",arg1.nroserie)
;    return this.http.get<InvMovAlmacenDet[]>(`${this.url}/get-all-inv-mov-almacen-det`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<InvMovAlmacenDet[]>(`${this.url}/get-all-inv-mov-almacen-det-combo`);
  }
  public getAllByInvMovAlmacenId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<InvMovAlmacenDet[]>(`${this.url}/get-all-inv-mov-almacen-det-by-inv-mov-almacen`,{params});
  } 
  public getAllByCnfProductoId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<InvMovAlmacenDet[]>(`${this.url}/get-all-inv-mov-almacen-det-by-cnf-producto`,{params});
  } 
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<InvMovAlmacenDet>(`${this.url}/get-inv-mov-almacen-det`,{params});
  }
  public save(form:any): Observable<InvMovAlmacenDet>{
    return this.http.post<InvMovAlmacenDet>(this.url+'/save-inv-mov-almacen-det',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-inv-mov-almacen-det', { observe: 'response' ,params}); 
  }
}

