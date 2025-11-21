package backend.ecommerce.ecommerce.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.ecommerce.ecommerce.entidades.Produto;

public interface ProdutoRepositorio extends JpaRepository<Produto, Long> {

    List<Produto> findByCategoriaId(Long categoriaId);
}
