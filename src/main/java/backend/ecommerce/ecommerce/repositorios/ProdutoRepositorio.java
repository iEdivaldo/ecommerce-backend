package backend.ecommerce.ecommerce.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import backend.ecommerce.ecommerce.entidades.Produto;

public interface ProdutoRepositorio extends JpaRepository<Produto, Long> {

    public List<Produto> findByCategoriaId(@Param("categoriaId") Long categoriaId);
}
