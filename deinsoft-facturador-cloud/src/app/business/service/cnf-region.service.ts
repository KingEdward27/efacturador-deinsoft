import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment.prod';
import { Observable } from 'rxjs/internal/Observable';
import { CnfRegion } from '../model/cnf-region.model';

@Injectable({
  providedIn: 'root'
})
export class CnfRegionService {
  url:string  = environment.apiUrl + '/api/business/cnf-region';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getAllData(arg1:CnfRegion):Observable<any>{
    let params = new HttpParams().set("nombre",arg1.nombre)
;    return this.http.get<CnfRegion[]>(`${this.url}/get-all-cnf-region`,{params});
  }
  public getAllDataCombo():Observable<any>{
    return this.http.get<CnfRegion[]>(`${this.url}/get-all-cnf-region-combo`);
  }
  public getAllByCnfPaisId(id:number):Observable<any>{
    let params = new HttpParams().set("id",id.toString());
    return this.http.get<CnfRegion[]>(`${this.url}/get-all-cnf-region-by-cnf-pais`,{params});
  } 
  public getData(arg1:string):Observable<any>{
    let params = new HttpParams().set("id",arg1)
    return this.http.get<CnfRegion>(`${this.url}/get-cnf-region`,{params});
  }
  public save(form:any): Observable<CnfRegion>{
    return this.http.post<CnfRegion>(this.url+'/save-cnf-region',form); 
  }
  public delete(arg1:string): Observable<HttpResponse<{}>>{
    let params = new HttpParams().set("id",arg1);
    return this.http.delete(this.url+'/delete-cnf-region', { observe: 'response' ,params}); 
  }
}

