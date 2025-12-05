package backend.ecommerce.ecommerce.servicos;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import backend.ecommerce.ecommerce.entidades.Notificacao;
import backend.ecommerce.ecommerce.entidades.TipoNotificacao;
import backend.ecommerce.ecommerce.entidades.Usuario;
import backend.ecommerce.ecommerce.repositorios.NotificacaoRepositorio;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final NotificacaoRepositorio notificacaoRepositorio;

    public void criarNotificacao(Usuario usuario, String titulo, String mensagem, TipoNotificacao tipo) {
        Notificacao notificacao = new Notificacao();
        notificacao.setUsuario(usuario);
        notificacao.setTitulo(titulo);
        notificacao.setMensagem(mensagem);
        notificacao.setTipo(tipo);
        notificacao.setLida(false);
        notificacao.setDataHora(LocalDateTime.now());
        notificacaoRepositorio.save(notificacao);
    }

    public List<Notificacao> listarNotificacoesPorUsuario(Long usuarioId) {
        return notificacaoRepositorio.findByUsuarioIdOrderByDataHoraDesc(usuarioId);
    }

    public List<Notificacao> listarNotificacoesNaoLidasPorUsuario(Long usuarioId) {
        return notificacaoRepositorio.findByUsuarioIdAndLidaFalseOrderByDataHoraDesc(usuarioId);
    }

    public long contarNotificacoesNaoLidasPorUsuario(Long usuarioId) {
        return notificacaoRepositorio.countByUsuarioIdAndLidaFalse(usuarioId);
    }

    public void marcarNotificacaoComoLida(Long notificacaoId) {
        Notificacao notificacao = notificacaoRepositorio.findById(notificacaoId)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));
        notificacao.setLida(true);
        notificacaoRepositorio.save(notificacao);
    }

    public void marcarTodasNotificacoesComoLidasPorUsuario(Long usuarioId) {
        List<Notificacao> notificacoes = notificacaoRepositorio.findByUsuarioIdAndLidaFalseOrderByDataHoraDesc(usuarioId);
        for (Notificacao notificacao : notificacoes) {
            notificacao.setLida(true);
        }
        notificacaoRepositorio.saveAll(notificacoes);
    }

    public List<Notificacao> buscarPorUsuario(Long id) {
        return notificacaoRepositorio.findByUsuarioIdOrderByDataHoraDesc(id);
    }

}
