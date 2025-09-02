package backend.ecommerce.ecommerce.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.ecommerce.ecommerce.entidades.Pedido;

public interface PedidoRepositorio extends JpaRepository<Pedido, Long> {}
