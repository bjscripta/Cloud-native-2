import { BrowserCacheLocation, InteractionType, IPublicClientApplication, PublicClientApplication } from '@azure/msal-browser';
import { MsalGuardConfiguration, MsalInterceptorConfiguration } from '@azure/msal-angular';
import { environment } from '../environments/environment';
export function msalInstanceFactory(): IPublicClientApplication {
  return new PublicClientApplication({ auth: { clientId: environment.frontendClientId, authority: `https://login.microsoftonline.com/${environment.tenantId}`, redirectUri: 'http://localhost:4200', postLogoutRedirectUri: 'http://localhost:4200' }, cache: { cacheLocation: BrowserCacheLocation.LocalStorage } });
}
export function msalGuardConfigFactory(): MsalGuardConfiguration {
  return { interactionType: InteractionType.Redirect, authRequest: { scopes: [environment.apiScope] }, loginFailedRoute: '/login' };
}
export function msalInterceptorConfigFactory(): MsalInterceptorConfiguration {
  return { interactionType: InteractionType.Redirect, protectedResourceMap: new Map([[`${environment.apiBaseUrl}/api/*`, [environment.apiScope]]]) };
}
