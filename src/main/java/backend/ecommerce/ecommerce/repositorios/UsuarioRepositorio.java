package backend.ecommerce.ecommerce.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.ecommerce.ecommerce.entidades.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    public boolean existsByEmail(String email);
}
