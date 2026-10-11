import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MsalService } from '@azure/msal-angular';
import { environment } from '../../environments/environment';
@Component({ selector: 'app-login', imports: [RouterLink], templateUrl: './login.html', styleUrl: './login.css' })
export class Login {
  constructor(private readonly authService: MsalService) {}
  get authenticated(): boolean { return this.authService.instance.getAllAccounts().length > 0; }
  login(): void { this.authService.loginRedirect({ scopes: [environment.apiScope] }); }
}
