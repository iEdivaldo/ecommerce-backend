package backend.ecommerce.ecommerce.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import backend.ecommerce.ecommerce.entidades.Pedido;

public interface PedidoRepositorio extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteIdOrderByCriadoEmDesc(Long clienteId);
    
    @Query("SELECT DISTINCT p FROM Pedido p JOIN p.itens i WHERE i.produto.usuarioCriacao.id = :vendedorId ORDER BY p.criadoEm DESC")
    List<Pedido> findPedidosPorVendedor(Long vendedorId);
}
