import { CnfEmpresa } from '@/business/model/cnf-empresa.model';
import { CnfEmpresaService } from '@/business/service/cnf-empresa.service';
import {AppState} from '@/store/state';
import {UiState} from '@/store/ui/state';
import {Component, HostBinding, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppService} from '@services/app.service';
import {Observable} from 'rxjs';

const BASE_CLASSES = 'main-sidebar elevation-4';
@Component({
    selector: 'app-menu-sidebar',
    templateUrl: './menu-sidebar.component.html',
    styleUrls: ['./menu-sidebar.component.scss']
})
export class MenuSidebarComponent implements OnInit {
    @HostBinding('class') classes: string = BASE_CLASSES;
    public ui: Observable<UiState>;
    public user;
    public menu;
    public perfil;
    public empresa;
    public local;
    constructor(
        public appService: AppService,
        private store: Store<AppState>
    ) {}

    ngOnInit() {
        
        this.ui = this.store.select('ui');
        this.ui.subscribe((state: UiState) => {
            this.classes = `${BASE_CLASSES} ${state.sidebarSkin}`;
        });
        this.user = this.appService.user;
        console.log(this.user);
        this.perfil = this.user.profile.split("|")[0]
        this.empresa = this.user.empresaPrincipal
        this.menu = this.appService.getMenu();
        console.log(this.menu);
        this.menu.forEach(element => {
            element.children = element.children.sort((n1,n2) => {
                if (n1.seqorder > n2.seqorder) {
                    return 1;
                }
            
                if (n1.seqorder < n2.seqorder) {
                    return -1;
                }
            
                return 0;
            });
        });
        
        //console.log(this.menu);
        
    }
}

