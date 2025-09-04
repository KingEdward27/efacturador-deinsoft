import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {MainComponent} from '@modules/main/main.component';
import {LoginComponent} from '@modules/login/login.component';
import {ProfileComponent} from '@pages/profile/profile.component';
import {RegisterComponent} from '@modules/register/register.component';
import {DashboardComponent} from '@pages/dashboard/dashboard.component';
import {AuthGuard} from '@guards/auth.guard';
import {NonAuthGuard} from '@guards/non-auth.guard';
import {ForgotPasswordComponent} from '@modules/forgot-password/forgot-password.component';
import {RecoverPasswordComponent} from '@modules/recover-password/recover-password.component';
import {PrivacyPolicyComponent} from '@modules/privacy-policy/privacy-policy.component';
import {MainMenuComponent} from '@pages/main-menu/main-menu.component';
import {SubMenuComponent} from '@pages/main-menu/sub-menu/sub-menu.component';
import { ActComprobanteListFormComponent } from '@pages/act-comprobante/act-comprobante-form/list/act-comprobante-list.component';
import { RcieListFormComponent } from '@pages/act-comprobante/act-comprobante-form/rcie/rcie-list.component';
import { RvieListFormComponent } from '@pages/act-comprobante/act-comprobante-form/rvie/rvie-list.component';
const routes: Routes = [
    {
        path: '',
        component: MainComponent,
        canActivate: [AuthGuard],
        canActivateChild: [AuthGuard],
        children: [
            {
                path: 'sub-menu-1',
                component: SubMenuComponent
            },
            {
                path: '',
                component: DashboardComponent
            },
            {path: 'comprobantes',component: ActComprobanteListFormComponent},
            {path: 'rcie',component: RcieListFormComponent},
            {path: 'rvie',component: RvieListFormComponent},
            {path: 'profile',component: ProfileComponent}
            
        ]
    },
    {
        path: 'login',
        component: LoginComponent,
        canActivate: [NonAuthGuard]
    },
    {
        path: 'register',
        component: RegisterComponent,
        canActivate: [NonAuthGuard]
    },
    {
        path: 'forgot-password',
        component: ForgotPasswordComponent,
        canActivate: [NonAuthGuard]
    },
    {
        path: 'recover-password',
        component: RecoverPasswordComponent,
        canActivate: [NonAuthGuard]
    },
    {
        path: 'privacy-policy',
        component: PrivacyPolicyComponent,
        canActivate: [NonAuthGuard]
    }
];

// @NgModule({
//     imports: [RouterModule.forRoot(routes, {relativeLinkResolution: 'legacy'})],
//     exports: [RouterModule]
// })
@NgModule({
    imports: [RouterModule.forRoot(routes,{ useHash:true,onSameUrlNavigation: 'reload' })],
    exports: [RouterModule]
  })
export class AppRoutingModule {}
