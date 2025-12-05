package backend.ecommerce.ecommerce.controles;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.ecommerce.ecommerce.autenticacao.dto.RegistrarRequest;
import backend.ecommerce.ecommerce.autenticacao.dto.TokenResponse;
import backend.ecommerce.ecommerce.entidades.Produto;
import backend.ecommerce.ecommerce.entidades.TipoNotificacao;
import backend.ecommerce.ecommerce.entidades.Usuario;
import backend.ecommerce.ecommerce.repositorios.ProdutoRepositorio;
import backend.ecommerce.ecommerce.servicos.NotificacaoService;
import backend.ecommerce.ecommerce.servicos.TokenService;
import backend.ecommerce.ecommerce.servicos.UsuarioService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/super_admin")
@RequiredArgsConstructor
public class SuperAdminController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final ProdutoRepositorio produtoRepositorio;
    private final NotificacaoService notificacaoService;
    
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/usuarios")
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/usuarios")
    public Map<String, Object> criarUsuario(@Valid @RequestBody RegistrarRequest request) {
        request.setSenha(passwordEncoder.encode(request.getSenha()));
        usuarioService.salvarUsuario(request);

        var acesso = tokenService.gerarAccessoToken(request.getEmail(), request.getPerfil().name());
        var refresh = tokenService.gerarRefreshToken(request.getEmail());
        return Map.of(
            "usuario", Map.of( "nome", request.getNome(), "email", request.getEmail(), "perfil", request.getPerfil()),
            "tokens", new TokenResponse(acesso, refresh)
        );
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/usuario/{idUsuario}")
    public Usuario obterUsuarioPorId(@PathVariable("idUsuario") Long idUsuario) {
        return usuarioService.obterUsuarioPorId(idUsuario);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/usuario/{id}")
    @Transactional
    public void deletarUsuario(@PathVariable("id") Long id) {
        produtoRepositorio.deleteByUsuarioCriacaoId(id);
        usuarioService.deletarUsuarioPorId(id);
    }
    
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/produto/{id}")
    @Transactional
    public void excluirProdutoComJustificativa(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String justificativa = body.get("justificativa");
        
        Produto produto = produtoRepositorio.findById(id).orElseThrow();
        Usuario vendedor = produto.getUsuarioCriacao();
        
        // Criar notificação para o vendedor
        String titulo = "Produto Excluído";
        String mensagem = String.format(
            "Seu produto '%s' foi excluído pelo administrador.\n\nMotivo: %s",
            produto.getNomeProduto(),
            justificativa
        );
        
        notificacaoService.criarNotificacao(vendedor, titulo, mensagem, TipoNotificacao.PRODUTO_EXCLUIDO);
        
        // Excluir o produto
        produtoRepositorio.deleteById(id);
    }
}
