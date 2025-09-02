package backend.ecommerce.ecommerce.controles;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.ecommerce.ecommerce.entidades.Pedido;
import backend.ecommerce.ecommerce.repositorios.PedidoRepositorio;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepositorio pedidoRepositorio;
    public PedidoController(PedidoRepositorio pedidoRepositorio) {
        this.pedidoRepositorio = pedidoRepositorio;
    }

    @GetMapping
    public List<Pedido> meusPedidos() {
        return pedidoRepositorio.findAll();
    }

    @PostMapping
    public Pedido criarPedido(@RequestBody Pedido pedido) {
        return pedidoRepositorio.save(pedido);
    }
    
}
