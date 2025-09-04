import { Injectable } from '@angular/core';
import {
    CanActivate,
    CanActivateChild,
    ActivatedRouteSnapshot,
    RouterStateSnapshot,
    UrlTree,
    Router
} from '@angular/router';
import { Observable } from 'rxjs';
import { AppService } from '@services/app.service';
import { AuthenticationService } from '@services/authentication.service';

@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanActivateChild {
    constructor(private router: Router, private appService: AppService, private authenticationService: AuthenticationService) { }

    canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ):
        | Observable<boolean | UrlTree>
        | Promise<boolean | UrlTree>
        | boolean
        | UrlTree {
        const isAuthenticated = this.authenticationService.isAuthenticated();

        if(!isAuthenticated){
            this.appService.logout() // , { queryParams: { returnUrl: state.url }}
            return false;
        }
        // return true;
        // console.log(state.url);
        let optionValid = []
        // return this.getProfile();
        let wa = this.appService.getMenu();
        // let url = state.url.split(";").length>0?state.url.split(";")[0].replace("/new-","/"):state.url.replace("/new-","/");
        // wa?.forEach(element => {
        //     if (!element.children) {
        //         if (element.path == url) {
        //             optionValid.push(element)
        //         }
        //     } else {
        //         //console.log(element.children);
        //         let wa = element.children.filter(
        //             item => item.path == url || item.children.filter(q => q.path == url).length > 0)

        //         if (wa.length > 0) {
        //             optionValid.push(wa);
        //         }

        //     }
        // }, err => {
        //     return false
        // });
        // console.log(state.url);
        // console.log(optionValid);
        optionValid = wa;
        if (optionValid.length > 0 || state.url == '/' 
        || state.url == '/generic-form' || state.url == '/generic-child-form' || state.url == '/profile' 
        || state.url == '/swagger-ui' || state.url == '/change-password' || state.url.includes('/recover-password')) {
            
            return this.getProfile();
        }
        else {
            return false;
        }
    }
    canActivateChild(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ):
        | Observable<boolean | UrlTree>
        | Promise<boolean | UrlTree>
        | boolean
        | UrlTree {
        return this.canActivate(next, state);
    }

    async getProfile() {
        // await this.appService.getProfile();
        // return true;
        if (this.appService.user) {
            return true;
        }

        try {
            await this.appService.getProfile();
            return true;
        } catch (error) {
            console.log(error);
            return false;
        }
    }
}
