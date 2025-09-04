import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UpdateParam } from '../components/model/UpdateParam';

export abstract class BaseService {
  public properties: any;
  protected baseEndpoint: string="";

  protected cabeceras: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor() { }
  
}
