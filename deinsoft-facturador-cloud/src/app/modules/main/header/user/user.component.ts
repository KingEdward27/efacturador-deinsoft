import {AfterViewInit, Component, OnInit} from '@angular/core';
import {AppService} from '@services/app.service';
import {DateTime} from 'luxon';

@Component({
    selector: 'app-user',
    templateUrl: './user.component.html',
    styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {
    public user;

    constructor(private appService: AppService) {}
    ngOnInit() {
        // console.log("asdf");
        // console.log(this.appService.user);
        
        this.user = this.appService.user;
        // console.log("asd: ", this.user);
        
    }

    logout() {
        this.appService.logout();
    }

    formatDate(date) {
        return DateTime.fromISO(date).toFormat('dd LLL yyyy');
    }
}
