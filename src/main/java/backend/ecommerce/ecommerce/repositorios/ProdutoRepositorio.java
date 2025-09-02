package backend.ecommerce.ecommerce.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.ecommerce.ecommerce.entidades.Produto;

public interface ProdutoRepositorio extends JpaRepository<Produto, Long> {}
