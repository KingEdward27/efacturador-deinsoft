import {Component} from '@angular/core';

@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.scss']
})
export class ProfileComponent {
    tabClass1:string="active tab-pane";
    tabClass2:string="tab-pane";
    tabClass3:string="tab-pane";
    setClass(order:number){
        if (order == 1) {
            this.tabClass1="active tab-pane";
            this.tabClass2="tab-pane";
            this.tabClass3="tab-pane";
        }
        if (order == 2) {
            this.tabClass1="tab-pane";
            this.tabClass2="active tab-pane";
            this.tabClass3="tab-pane";
        }
        if (order == 3) {
            this.tabClass1="tab-pane";
            this.tabClass2="tab-pane";
            this.tabClass3="active tab-pane";
        }
    }
}
