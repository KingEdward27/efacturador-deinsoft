import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { UtilService } from '@services/util.service';
import { BnNgIdleService } from 'bn-ng-idle'; 

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'ventas-app';
  mySubscription;
  
  constructor(private router: Router, private translate: TranslateService, 
    private utilService: UtilService, private bnIdle: BnNgIdleService) {
    this.setAppLanguage();
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.mySubscription = this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        // Trick the Router into believing it's last link wasn't previously loaded
        this.router.navigated = false;
      }
    });
  }
  ngOnInit(): void {
    //60 = 1 minute
    this.bnIdle.startWatching(60).subscribe((res) => {
      if (res) {
        console.log('session expired');
        // this.router.navigateByUrl('logout');
      }
    });
  }
  setAppLanguage() {
    this.translate.addLangs(['es', 'en']);
    this.translate.setDefaultLang("es");
    let lang = localStorage.getItem('lang');
    this.translate.use(lang ? lang : "es");
    // this.translate.use(this.translate.getBrowserLang());

    // const browserLang = this.translate.getBrowserLang();
    // this.translate.use(browserLang.match(/es|en/) ? browserLang : 'en');
  }
}
