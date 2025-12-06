package backend.ecommerce.ecommerce.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.ecommerce.ecommerce.entidades.Endereco;

public interface EnderecoRepositorio extends JpaRepository<Endereco, Long>{

    List<Endereco> findByUsuarioId(Long usuarioId);
}