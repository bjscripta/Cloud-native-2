package com.pedidos360.infraestructuredomain.bff.client;

import com.pedidos360.infraestructuredomain.bff.dto.FacturacionDTO;
import java.time.Duration;
import java.util.List;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class FacturacionClient {
    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private final WebClient webClient;

    public FacturacionClient(WebClient.Builder builder) {
        webClient = builder.clone().baseUrl("http://facturacion-service")
                .defaultStatusHandler(HttpStatusCode::isError,
                        response -> Mono.just(new ResponseStatusException(response.statusCode(), "Error del microservicio de facturación")))
                .build();
    }

    public List<FacturacionDTO> listar() { return webClient.get().uri("/facturas").retrieve().bodyToFlux(FacturacionDTO.class).collectList().block(TIMEOUT); }
    public FacturacionDTO buscar(Long id) { return webClient.get().uri("/facturas/{id}", id).retrieve().bodyToMono(FacturacionDTO.class).block(TIMEOUT); }
    public FacturacionDTO buscarPorPedido(Long pedidoId) { return webClient.get().uri("/facturas/pedido/{pedidoId}", pedidoId).retrieve().bodyToMono(FacturacionDTO.class).block(TIMEOUT); }
    public FacturacionDTO crear(FacturacionDTO dto) { return webClient.post().uri("/facturas").bodyValue(dto).retrieve().bodyToMono(FacturacionDTO.class).block(TIMEOUT); }
    public FacturacionDTO actualizar(Long id, FacturacionDTO dto) { return webClient.put().uri("/facturas/{id}", id).bodyValue(dto).retrieve().bodyToMono(FacturacionDTO.class).block(TIMEOUT); }
    public String eliminar(Long id) { return webClient.delete().uri("/facturas/{id}", id).retrieve().bodyToMono(String.class).block(TIMEOUT); }
}
