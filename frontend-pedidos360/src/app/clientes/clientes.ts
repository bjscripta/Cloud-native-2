import { JsonPipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit, signal } from '@angular/core';
import { environment } from '../../environments/environment';
@Component({ selector: 'app-clientes', imports: [JsonPipe], templateUrl: './clientes.html', styleUrl: './clientes.css' })
export class Clientes implements OnInit {
  readonly clientes = signal<unknown[]>([]);
  readonly cargando = signal(true);
  readonly error = signal('');
  constructor(private readonly http: HttpClient) {}
  ngOnInit(): void {
    this.http.get<unknown[]>(`${environment.apiBaseUrl}/api/clientes`).subscribe({
      next: (clientes) => { this.clientes.set(clientes); this.cargando.set(false); },
      error: (error) => { this.error.set(`No fue posible consultar clientes (HTTP ${error.status || 'desconocido'}).`); this.cargando.set(false); },
    });
  }
}
