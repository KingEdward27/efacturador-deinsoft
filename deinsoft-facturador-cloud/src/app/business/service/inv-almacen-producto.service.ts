import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { InvAlmacenProducto } from '../model/inv-almacen-producto.model';

@Injectable({
  providedIn: 'root'
})
export class InvAlmacenProductoService {
  url:string  = environment.apiUrl + '/api/business/inv-almacen-producto';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:InvAlmacenProducto):Observable<any>{
    let params = new HttpParams();    return this.http.get<InvAlmacenProducto[]>(`${this.url}/get-all-inv-almacen-producto`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<InvAlmacenProducto[]>(`${this.url}/get-all-inv-almacen-producto-combo`);
  }
  public getAllByInvAlmacenId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<InvAlmacenProducto[]>(`${this.url}/get-all-inv-almacen-producto-by-inv-almacen`,{params});
  } 
  public getAllByCnfProductoId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<InvAlmacenProducto[]>(`${this.url}/get-all-inv-almacen-producto-by-cnf-producto`,{params});
  } 
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<InvAlmacenProducto>(`${this.url}/get-inv-almacen-producto`,{params});
  }
  public save(form:any): Observable<InvAlmacenProducto>{
    return this.http.post<InvAlmacenProducto>(this.url+'/save-inv-almacen-producto',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-inv-almacen-producto', { observe: 'response' ,params}); 
  }
  public getReport(form: any): Observable<InvAlmacenProducto> {
    return this.http.post<InvAlmacenProducto>(this.url + '/get-report-inv-almacen-producto', form);
  }
}

