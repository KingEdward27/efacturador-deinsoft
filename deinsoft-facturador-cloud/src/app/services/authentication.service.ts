import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { HttpClient, HttpHeaders, HttpParams, JsonpClientBackend } from '@angular/common/http';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Router } from '@angular/router';
import { environment } from 'environments/environment';
import { AnyFn } from '@ngrx/store/src/selector';

const helper = new JwtHelperService();

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  url = environment.apiUrl;
  
  private TOKEN_NAME = 'dfc-token'
  constructor(private http: HttpClient, private router: Router) {
  }
  login(name: string, password: string): Promise<any> {

    var headers = new HttpHeaders();
    headers.append("Accept", 'application/json');
    headers.append('Content-Type', 'application/json');
    headers.append('Access-Control-Allow-Origin', '*');

    let data = {
      headers: headers,
      observe: 'response' as 'body',
    }
    return this.http.post<any>(this.url + '/login', { name, password }, data)
      .pipe(map(data => {
        console.log(data.headers);
        
        return data.headers.get('authorization');
      }, (error: any) => { console.log("ERRORN: ", error) })).toPromise();
  }

  register(form: any): Promise<any> {

    var headers = new HttpHeaders();
    headers.append("Accept", 'application/json');
    headers.append('Content-Type', 'application/json');
    headers.append('Access-Control-Allow-Origin', '*');

    let data = {
      headers: headers,
      observe: 'response' as 'body',
    }
    return this.http.post<any>(this.url + '/register', { ruc: form.ruc, name: form.name, password: form.password }, data)
      .pipe(map(data => {
        return data.headers.get('authorization');
      }, (error: any) => { console.log("ERRORN: ", error) })).toPromise();
  }
  public getToken(): any {
    if (sessionStorage.getItem(this.TOKEN_NAME) == null) {
      return "";
    }
    return sessionStorage.getItem(this.TOKEN_NAME);
  }
  public isAuthenticated(): boolean {
    const token = this.getToken();

    return !helper.isTokenExpired(token) //tokenNotExpired(null,token);
  }
}
