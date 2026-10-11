package com.pedidos360.infraestructuredomain.bff.controller;

import com.pedidos360.infraestructuredomain.bff.client.EnvioClient;
import com.pedidos360.infraestructuredomain.bff.dto.EnvioDTO;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/envios")
public class EnvioBffController {
    private final EnvioClient client;
    public EnvioBffController(EnvioClient client) { this.client = client; }
    @GetMapping public List<EnvioDTO> listar() { return client.listar(); }
    @GetMapping("/{id}") public EnvioDTO buscar(@PathVariable Long id) { return client.buscar(id); }
    @GetMapping("/pedido/{pedidoId}") public EnvioDTO buscarPorPedido(@PathVariable Long pedidoId) { return client.buscarPorPedido(pedidoId); }
    @PostMapping public EnvioDTO crear(@RequestBody EnvioDTO dto) { return client.crear(dto); }
    @PutMapping("/{id}") public EnvioDTO actualizar(@PathVariable Long id, @RequestBody EnvioDTO dto) { return client.actualizar(id, dto); }
    @DeleteMapping("/{id}") public String eliminar(@PathVariable Long id) { return client.eliminar(id); }
}
