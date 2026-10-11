package com.pedidos360.infraestructuredomain.bff.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FacturacionDTO {
    private Long id;
    private Long pedidoId;
    private Long clienteId;
    private double montoTotal;
    private String estadoPago;
}
