import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';

export class ApiDniResult {
	apellidoPaterno: string;
	apellidoMaterno: string;
	nombres: string;
};


@Injectable({
  providedIn: 'root'
})
export class ApiDniService {
  url:string  = 'https://api.apis.net.pe/v1/dni';
  constructor(private http: HttpClient,
              private router: Router) { 
  }

  public getDataByTipoDoc(tipoDoc:string, doc:string):Observable<any>{
    if (tipoDoc == '1') {
      this.url = this.url + "/dni"
    } else if (tipoDoc == '6') {
      this.url = this.url + "/ruc"
    }
    let params = new HttpParams().set("numero",doc)
    return this.http.get<ApiDniResult>(`${this.url}`,{params});
  }
}

