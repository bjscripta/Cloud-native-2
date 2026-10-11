import { Routes } from '@angular/router';
import { MsalGuard } from '@azure/msal-angular';
import { Login } from './login/login';
import { Clientes } from './clientes/clientes';
export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'clientes', component: Clientes, canActivate: [MsalGuard] },
  { path: '', pathMatch: 'full', redirectTo: 'login' },
  { path: '**', redirectTo: 'login' },
];
