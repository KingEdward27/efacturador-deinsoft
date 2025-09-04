import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { CnfProducto } from '../model/cnf-producto.model';

@Injectable({
  providedIn: 'root'
})
export class CnfProductoService {
  url: string = environment.apiUrl + '/api/business/cnf-producto';
  constructor(private http: HttpClient,
    private router: Router) {
  }

  public getAllData(arg1: CnfProducto): Observable<any> {
    let params = new HttpParams().set("codigo", arg1.codigo)
      .set("nombre", arg1.nombre)
      .set("rutaImagen", arg1.rutaImagen)
      .set("flagEstado", arg1.flagEstado)
      .set("barcode", arg1.barcode)
      ; return this.http.get<CnfProducto[]>(`${this.url}/get-all-cnf-producto`, { params });
  }
  public getAllDataCombo(): Observable<any> {
    return this.http.get<CnfProducto[]>(`${this.url}/get-all-cnf-producto-combo`);
  }
  public getAllByCnfUnidadMedidaId(id: number): Observable<any> {
    let params = new HttpParams().set("id", id.toString());
    return this.http.get<CnfProducto[]>(`${this.url}/get-all-cnf-producto-by-cnf-unidad-medida`, { params });
  }
  public getAllByCnfEmpresaId(id: number): Observable<any> {
    let params = new HttpParams().set("id", id.toString());
    return this.http.get<CnfProducto[]>(`${this.url}/get-all-cnf-producto-by-cnf-empresa`, { params });
  }
  public getAllByCnfSubCategoriaId(id: number): Observable<any> {
    let params = new HttpParams().set("id", id.toString());
    return this.http.get<CnfProducto[]>(`${this.url}/get-all-cnf-producto-by-cnf-sub-categoria`, { params });
  }
  public getAllByCnfMarcaId(id: number): Observable<any> {
    let params = new HttpParams().set("id", id.toString());
    return this.http.get<CnfProducto[]>(`${this.url}/get-all-cnf-producto-by-cnf-marca`, { params });
  }
  public getData(arg1: string): Observable<any> {
    let params = new HttpParams().set("id", arg1)
    return this.http.get<CnfProducto>(`${this.url}/get-cnf-producto`, { params });
  }
  public save(form: any): Observable<CnfProducto> {
    return this.http.post<CnfProducto>(this.url + '/save-cnf-producto', form);
  }
  public delete(arg1: string): Observable<HttpResponse<{}>> {
    let params = new HttpParams().set("id", arg1);
    return this.http.delete(this.url + '/delete-cnf-producto', { observe: 'response', params });
  }
  public getAllDataComboTypeHead(name_value: string,cnfEmpresaId:number): Observable<any> {
    let params = new HttpParams()
    .set("nameOrCode",name_value)
    .set("cnfEmpresaId",cnfEmpresaId.toString());
    return this.http.get<CnfProducto[]>(`${this.url}/get-all-cnf-producto-typehead`, { params });
  }
  public getAllDataComboTypeHeadNoServicios(name_value: string,cnfEmpresaId:number): Observable<any> {
    let params = new HttpParams()
    .set("nameOrCode",name_value)
    .set("cnfEmpresaId",cnfEmpresaId.toString());
    return this.http.get<CnfProducto[]>(`${this.url}/get-all-cnf-producto-typehead-no-servicios`, { params });
  }
  public getPdfCodeBarsPre(jsonData:any): Observable<any> {
    return this.http.post<CnfProducto[]>(`${this.url}/get-all-cnf-producto-getpdf-codebars-pre`, jsonData);
  }
  public getPdfCodeBars(jsonData:any): Observable<any> {
    return this.http.post(`${this.url}/getpdf-codebars`, jsonData,{observe: 'response', responseType: 'blob'});
  }
}

