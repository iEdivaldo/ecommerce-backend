package backend.ecommerce.ecommerce.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import backend.ecommerce.ecommerce.entidades.Endereco;

public interface EnderecoRepositorio extends JpaRepository<Endereco, Long>{

    @Query("SELECT e FROM Endereco e WHERE e.usuario.id = :usuarioId ORDER BY e.padrao DESC, e.cidade ASC")
    List<Endereco> findByUsuario(@Param("usuarioId") Long usuarioId);

    @Modifying
    @Query("UPDATE Endereco e SET e.padrao = false WHERE e.usuario.id = :usuarioId AND e.padrao = true")
    void desmarcarEnderecoPadrao(@Param("usuarioId") Long usuarioId);
}