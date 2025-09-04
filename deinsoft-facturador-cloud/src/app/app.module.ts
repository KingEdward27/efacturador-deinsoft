import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClient, HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from '@/app-routing.module';
import { AppComponent } from './app.component';
import { MainComponent } from '@modules/main/main.component';
import { LoginComponent } from '@modules/login/login.component';
import { HeaderComponent } from '@modules/main/header/header.component';
import { FooterComponent } from '@modules/main/footer/footer.component';
import { MenuSidebarComponent } from '@modules/main/menu-sidebar/menu-sidebar.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ProfileComponent } from '@pages/profile/profile.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RegisterComponent } from '@modules/register/register.component';
import { DashboardComponent } from '@pages/dashboard/dashboard.component';
import { ToastrModule } from 'ngx-toastr';
import { MessagesComponent } from '@modules/main/header/messages/messages.component';
import { NotificationsComponent } from '@modules/main/header/notifications/notifications.component';
import { ButtonComponent } from './components/button/button.component';

import { registerLocaleData } from '@angular/common';
import localeEn from '@angular/common/locales/en';
import { UserComponent } from '@modules/main/header/user/user.component';
import { ForgotPasswordComponent } from '@modules/forgot-password/forgot-password.component';
import { RecoverPasswordComponent } from '@modules/recover-password/recover-password.component';
import { LanguageComponent } from '@modules/main/header/language/language.component';
import { PrivacyPolicyComponent } from './modules/privacy-policy/privacy-policy.component';
import { MainMenuComponent } from './pages/main-menu/main-menu.component';
import { SubMenuComponent } from './pages/main-menu/sub-menu/sub-menu.component';
import { MenuItemComponent } from './components/menu-item/menu-item.component';
import { DropdownComponent } from './components/dropdown/dropdown.component';
import { DropdownMenuComponent } from './components/dropdown/dropdown-menu/dropdown-menu.component';
import { ControlSidebarComponent } from './modules/main/control-sidebar/control-sidebar.component';
import { StoreModule } from '@ngrx/store';
import { authReducer } from './store/auth/reducer';
import { uiReducer } from './store/ui/reducer';
import { NgbModule, NgbPopoverModule } from '@ng-bootstrap/ng-bootstrap';
import { SelectComponent } from './components/select/select.component';
import { CheckboxComponent } from './components/checkbox/checkbox.component';
import { GenericListComponent } from './base/components/generic-list/generic-list.component';
import { GenericFormComponent } from './base/components/generic-form/generic-form.component';
import { GenericMasterDetailFormComponent } from './base/components/generic-master-detail-form/generic-master-detail-form.component';
import { GenericModalComponent } from './base/components/generic-modal/generic-modal.component';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { translatePartialLoader } from './config/translation.config';
import { ActCompraFormComponent } from '@pages/act-compra/act-compra-form.component';
import { JwtInterceptor } from './config/jwt.interceptor';
import { RptActCompraComponent } from '@pages/reports/act-compra/rpt-act-compra.component';
import { GenericList2Component } from './base/components/generic-list2/generic-list2.component';
import { GenericChildFormComponent } from './base/components/generic-child/generic-child-form.component';
import { SegUsuarioComponent } from '@pages/security/seg-usuario/seg_usuario';
import { SegRolComponent } from '@pages/security/seg-rol/seg_rol';
import { LoadingBarHttpClientModule } from '@ngx-loading-bar/http-client';
import { LoadingBarRouterModule } from '@ngx-loading-bar/router';
import { MessageModalComponent } from '@pages/act-comprobante/modal/message-modal.component';
import { ActComprobanteCompraReportFormComponent } from '@pages/reports/act-comprobante/act-comprobante-compra/act-comprobante-compra-report.component';
import { ActComprobanteReportFormComponent } from '@pages/reports/act-comprobante/act-comprobante-venta/act-comprobante-report.component';
import { ActComprobanteListFormComponent } from '@pages/act-comprobante/act-comprobante-form/list/act-comprobante-list.component';
import { NgSelectModule } from '@ng-select/ng-select';
import { SegUsuarioEmpresaComponent } from '@pages/security/seg-usuario-empresa/seg_usuario';
import { SegMenuFormComponent } from '@pages/security/seg-menu/form/seg-menu-form.component';
import { SegMenuListComponent } from '@pages/security/seg-menu/list/seg-menu-list.component';
import { SegAccionFormComponent } from '@pages/security/seg-accion/form/seg-accion-form.component';
import { SegAccionListComponent } from '@pages/security/seg-accion/list/seg-accion-list.component';
import { SegPermisoFormComponent } from '@pages/security/seg-permiso/form/seg-permiso-form.component';
import { SegPermisoListComponent } from '@pages/security/seg-permiso/list/seg-permiso-list.component';
import { SegAccionComponent } from '@pages/security/seg-accion/seg_accion';
import { ActPagoProgramacionReportComponent } from '@pages/reports/act-pago-programacion/act-pago-programacion-report.component';
import { BnNgIdleService } from 'bn-ng-idle';
registerLocaleData(localeEn, 'en-EN');
import { ActComprobanteFormComponent } from './pages/act-comprobante/act-comprobante-form/form/act-comprobante-form.component';
import { OpeGenericFormModalComponent } from './pages/modal/ope-generic-form-modal.component';
import { GenericList3Component } from './base/components/generic-list3/generic-list3.component';
import { GenericForm2Component } from './base/components/generic-form2/generic-form2.component';
import { SegPermisoForm2Component } from '@pages/security/seg-permiso/form2/seg-permiso-form2.component';
import { RcieListFormComponent } from '@pages/act-comprobante/act-comprobante-form/rcie/rcie-list.component';
import { RvieListFormComponent } from '@pages/act-comprobante/act-comprobante-form/rvie/rvie-list.component';

@NgModule({
    declarations: [
        AppComponent,
        MainComponent,
        LoginComponent,
        HeaderComponent,
        FooterComponent,
        MenuSidebarComponent,
        ProfileComponent,
        RegisterComponent,
        DashboardComponent,
        MessagesComponent,
        NotificationsComponent,
        ButtonComponent,
        UserComponent,
        ForgotPasswordComponent,
        RecoverPasswordComponent,
        LanguageComponent,
        PrivacyPolicyComponent,
        MainMenuComponent,
        SubMenuComponent,
        MenuItemComponent,
        DropdownComponent,
        DropdownMenuComponent,
        ControlSidebarComponent,
        SelectComponent,
        CheckboxComponent,
        GenericListComponent,
        GenericFormComponent,
        GenericMasterDetailFormComponent,
        GenericModalComponent,

        GenericModalComponent,
        ActCompraFormComponent,
        SegUsuarioComponent,
        SegRolComponent,
        RptActCompraComponent,
        GenericList2Component,
        GenericChildFormComponent,
        ActComprobanteFormComponent,
        MessageModalComponent,
        ActComprobanteReportFormComponent,
        ActComprobanteCompraReportFormComponent,
        ActComprobanteListFormComponent,
        SegUsuarioEmpresaComponent,
        SegMenuFormComponent,
        SegMenuListComponent,
        SegAccionFormComponent,
        SegAccionListComponent,
        SegPermisoFormComponent,
        SegPermisoListComponent,
        SegAccionComponent,
        ActPagoProgramacionReportComponent,
        OpeGenericFormModalComponent,
        GenericList3Component,
        GenericForm2Component,
        SegPermisoForm2Component,
        RcieListFormComponent,
        RvieListFormComponent
    ],
    imports: [
        BrowserModule,
        StoreModule.forRoot({ auth: authReducer, ui: uiReducer }),
        HttpClientModule,
        AppRoutingModule,
        ReactiveFormsModule,
        FormsModule,
        NgSelectModule,
        NgbPopoverModule,
        BrowserAnimationsModule,
        ToastrModule.forRoot({
            timeOut: 3000,
            positionClass: 'toast-top-right',
            preventDuplicates: true
        }),
        NgbModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: translatePartialLoader,
                deps: [HttpClient],
            }
        }),
        // for HttpClient use:
        LoadingBarHttpClientModule,

        // for Router use:
        LoadingBarRouterModule,

    ],
    providers: [
        HttpClientModule,
        { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
        [BnNgIdleService]
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
export function HttpLoaderFactory(http: HttpClient) {
    return new TranslateHttpLoader(http);
}
