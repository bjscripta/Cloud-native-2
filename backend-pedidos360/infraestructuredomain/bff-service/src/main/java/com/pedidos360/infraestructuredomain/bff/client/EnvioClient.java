package com.pedidos360.infraestructuredomain.bff.client;

import com.pedidos360.infraestructuredomain.bff.dto.EnvioDTO;
import java.time.Duration;
import java.util.List;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class EnvioClient {
    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private final WebClient webClient;

    public EnvioClient(WebClient.Builder builder) {
        webClient = builder.clone().baseUrl("http://envios-service")
                .defaultStatusHandler(HttpStatusCode::isError,
                        response -> Mono.just(new ResponseStatusException(response.statusCode(), "Error del microservicio de envíos")))
                .build();
    }

    public List<EnvioDTO> listar() { return webClient.get().uri("/envios").retrieve().bodyToFlux(EnvioDTO.class).collectList().block(TIMEOUT); }
    public EnvioDTO buscar(Long id) { return webClient.get().uri("/envios/{id}", id).retrieve().bodyToMono(EnvioDTO.class).block(TIMEOUT); }
    public EnvioDTO buscarPorPedido(Long pedidoId) { return webClient.get().uri("/envios/pedido/{pedidoId}", pedidoId).retrieve().bodyToMono(EnvioDTO.class).block(TIMEOUT); }
    public EnvioDTO crear(EnvioDTO dto) { return webClient.post().uri("/envios").bodyValue(dto).retrieve().bodyToMono(EnvioDTO.class).block(TIMEOUT); }
    public EnvioDTO actualizar(Long id, EnvioDTO dto) { return webClient.put().uri("/envios/{id}", id).bodyValue(dto).retrieve().bodyToMono(EnvioDTO.class).block(TIMEOUT); }
    public String eliminar(Long id) { return webClient.delete().uri("/envios/{id}", id).retrieve().bodyToMono(String.class).block(TIMEOUT); }
}
