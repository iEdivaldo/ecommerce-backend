package backend.ecommerce.ecommerce.controles;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.ecommerce.ecommerce.entidades.Notificacao;
import backend.ecommerce.ecommerce.entidades.Usuario;
import backend.ecommerce.ecommerce.repositorios.UsuarioRepositorio;
import backend.ecommerce.ecommerce.servicos.NotificacaoService;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    private final UsuarioRepositorio usuarioRepositorio;

    @GetMapping
    public ResponseEntity<List<Notificacao>> listarNotificacoes(Authentication authentication) {
        Usuario usuario = getUsuarioFromAuth(authentication);
        List<Notificacao> notificacoes = notificacaoService.buscarPorUsuario(usuario.getId());
        return ResponseEntity.ok(notificacoes);
    }

    @GetMapping("/nao-lidas")
    public ResponseEntity<List<Notificacao>> listarNotificacoesNaoLidas(Authentication authentication) {
        Usuario usuario = getUsuarioFromAuth(authentication);
        return ResponseEntity.ok(notificacaoService.listarNotificacoesNaoLidasPorUsuario(usuario.getId()));
    }

    @GetMapping("/contar-nao-lidas")
    public ResponseEntity<Long> contarNotificacoesNaoLidas(Authentication authentication) {
        Usuario usuario = getUsuarioFromAuth(authentication);
        return ResponseEntity.ok(notificacaoService.contarNotificacoesNaoLidasPorUsuario(usuario.getId()));
    }

    @PutMapping("/{id}/marcar-como-lida")
    public ResponseEntity<Void> marcarNotificacaoComoLida(@PathVariable Long id, Authentication authentication) {
        Usuario usuario = getUsuarioFromAuth(authentication);
        notificacaoService.marcarNotificacaoComoLida(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/marcar-todas-como-lidas")
    public ResponseEntity<Void> marcarTodasNotificacoesComoLidas(Authentication authentication) {
        Usuario usuario = getUsuarioFromAuth(authentication);
        notificacaoService.marcarTodasNotificacoesComoLidasPorUsuario(usuario.getId());
        return ResponseEntity.ok().build();
    }

    private Usuario getUsuarioFromAuth(Authentication authentication) {
        String email = authentication.getName();
        return usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}
