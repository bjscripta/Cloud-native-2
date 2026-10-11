package com.pedidos360.infraestructuredomain.bff.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnvioDTO {
    private Long id;
    private Long pedidoId;
    private String direccionEntrega;
    private String estadoEnvio;
    private String numeroSeguimiento;
}
