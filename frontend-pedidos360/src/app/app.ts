import { Component, OnInit, signal } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { EventMessage, EventType } from '@azure/msal-browser';
import { MsalBroadcastService, MsalService } from '@azure/msal-angular';
import { filter } from 'rxjs';

@Component({ selector: 'app-root', imports: [RouterLink, RouterOutlet], templateUrl: './app.html', styleUrl: './app.css' })
export class App implements OnInit {
  readonly isAuthenticated = signal(false);
  readonly accountName = signal('');
  constructor(private readonly authService: MsalService, private readonly broadcastService: MsalBroadcastService) {}
  ngOnInit(): void {
    this.authService.handleRedirectObservable().subscribe({
      next: (result) => {
        if (result?.account) this.authService.instance.setActiveAccount(result.account);
        this.updateAccount();
      },
    });
    this.broadcastService.msalSubject$.pipe(filter((event: EventMessage) =>
      event.eventType === EventType.LOGIN_SUCCESS || event.eventType === EventType.LOGOUT_SUCCESS
    )).subscribe(() => this.updateAccount());
  }
  logout(): void { this.authService.logoutRedirect(); }
  private updateAccount(): void {
    const accounts = this.authService.instance.getAllAccounts();
    const account = this.authService.instance.getActiveAccount() ?? accounts[0];
    if (account && !this.authService.instance.getActiveAccount()) this.authService.instance.setActiveAccount(account);
    this.isAuthenticated.set(Boolean(account));
    this.accountName.set(account?.name ?? account?.username ?? '');
  }
}
