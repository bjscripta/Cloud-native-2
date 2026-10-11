package com.pedidos360.infraestructuredomain.bff.controller;

import com.pedidos360.infraestructuredomain.bff.client.FacturacionClient;
import com.pedidos360.infraestructuredomain.bff.dto.FacturacionDTO;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/facturas")
public class FacturacionBffController {
    private final FacturacionClient client;
    public FacturacionBffController(FacturacionClient client) { this.client = client; }
    @GetMapping public List<FacturacionDTO> listar() { return client.listar(); }
    @GetMapping("/{id}") public FacturacionDTO buscar(@PathVariable Long id) { return client.buscar(id); }
    @GetMapping("/pedido/{pedidoId}") public FacturacionDTO buscarPorPedido(@PathVariable Long pedidoId) { return client.buscarPorPedido(pedidoId); }
    @PostMapping public FacturacionDTO crear(@RequestBody FacturacionDTO dto) { return client.crear(dto); }
    @PutMapping("/{id}") public FacturacionDTO actualizar(@PathVariable Long id, @RequestBody FacturacionDTO dto) { return client.actualizar(id, dto); }
    @DeleteMapping("/{id}") public String eliminar(@PathVariable Long id) { return client.eliminar(id); }
}
