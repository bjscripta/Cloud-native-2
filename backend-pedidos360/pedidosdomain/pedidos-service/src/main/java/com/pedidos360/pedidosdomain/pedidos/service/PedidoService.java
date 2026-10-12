package com.pedidos360.pedidosdomain.pedidos.service;

import com.pedidos360.pedidosdomain.pedidos.model.Pedido;
import com.pedidos360.pedidosdomain.pedidos.messaging.PedidoEventPublisher;
import com.pedidos360.pedidosdomain.pedidos.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoEventPublisher pedidoEventPublisher;

    public List<Pedido> getPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido getPedidoById(int id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pedido no encontrado"));
    }

    public List<Pedido> getPedidosByClienteId(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    public Pedido savePedido(Pedido pedido) {
        boolean nuevo = pedido.getId() == 0;
        Pedido guardado = pedidoRepository.save(pedido);
        if (nuevo) {
            pedidoEventPublisher.publicarPedidoCreado(guardado);
        }
        return guardado;
    }

    public String deletePedido(int id) {
        Pedido pedido = getPedidoById(id);
        pedidoRepository.delete(pedido);
        return "Pedido eliminado";
    }
}
