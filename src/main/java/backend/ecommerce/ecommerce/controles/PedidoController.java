package backend.ecommerce.ecommerce.controles;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.ecommerce.ecommerce.entidades.Pedido;
import backend.ecommerce.ecommerce.entidades.StatusPedido;
import backend.ecommerce.ecommerce.entidades.Usuario;
import backend.ecommerce.ecommerce.entidades.dto.CriarPedidoDTO;
import backend.ecommerce.ecommerce.repositorios.UsuarioRepositorio;
import backend.ecommerce.ecommerce.servicos.PedidoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final UsuarioRepositorio usuarioRepositorio;

    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/meus-pedidos")
    public ResponseEntity<List<Pedido>> meusPedidos(Authentication authentication) {
        Usuario cliente = getUsuarioFromAuth(authentication);
        List<Pedido> pedidos = pedidoService.listarPedidosPorCliente(cliente.getId());
        return ResponseEntity.ok(pedidos);
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody CriarPedidoDTO dto, Authentication authentication) {
        Usuario cliente = getUsuarioFromAuth(authentication);
        Pedido pedidoCriado = pedidoService.criarPedido(dto, cliente);
        return ResponseEntity.ok(pedidoCriado);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/minhas-vendas")
    public ResponseEntity<List<Pedido>> minhasVendas(Authentication authentication) {
        Usuario administrador = getUsuarioFromAuth(authentication);
        List<Pedido> vendas = pedidoService.listarPedidosVendedor(administrador.getId());
        return ResponseEntity.ok(vendas);
    }

    @PreAuthorize("hasAnyRole('CLIENTE') or hasRole('ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obterPedidoPorId(@PathVariable("id") Long id, Authentication authentication) {
        Usuario usuario = getUsuarioFromAuth(authentication);
        Pedido pedido = pedidoService.obterPedidoPorId(id, usuario.getId());
        return ResponseEntity.ok(pedido);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}/status")
    public ResponseEntity<Pedido> atualizarStatusPedido(@PathVariable("id") Long id,
            @RequestBody StatusPedido novoStatus, Authentication authentication) {
        Usuario vendedor = getUsuarioFromAuth(authentication);
        pedidoService.atualizarStatusPedido(id, novoStatus, vendedor.getId());
        return ResponseEntity.ok().build();
    }

    private Usuario getUsuarioFromAuth(Authentication authentication) {
        String email = authentication.getName();
        return usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    
}
