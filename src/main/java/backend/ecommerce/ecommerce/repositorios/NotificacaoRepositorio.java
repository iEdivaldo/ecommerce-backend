package backend.ecommerce.ecommerce.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.ecommerce.ecommerce.entidades.Notificacao;

public interface NotificacaoRepositorio extends JpaRepository<Notificacao, Long> {
    List<Notificacao> findByUsuarioIdOrderByDataHoraDesc(Long usuarioId);
    List<Notificacao> findByUsuarioIdAndLidaFalseOrderByDataHoraDesc(Long usuarioId);
    long countByUsuarioIdAndLidaFalse(Long usuarioId);
}
