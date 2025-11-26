package backend.ecommerce.ecommerce.controles;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.ecommerce.ecommerce.entidades.Usuario;
import backend.ecommerce.ecommerce.servicos.UsuarioConfigService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/configuracoes")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioConfigService usuarioService;

    @PutMapping()
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMINISTRADOR')")
    public ResponseEntity<?> atualizarConfiguracoes(@RequestBody ConfiguracoesRequest request) {
        try {
            Usuario usuario = usuarioService.atualizarConfiguracoes(
                request.getNome(),
                request.getEmail(),
                request.getSenhaAtual(),
                request.getNovaSenha()
            );
            return ResponseEntity.ok(new ConfiguracoesResponse(
                usuario.getNome(),
                usuario.getEmail(),
                "Configurações atualizadas com sucesso."
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @Data
    public static class ConfiguracoesRequest {
        private String nome;
        private String email;
        private String senhaAtual;
        private String novaSenha;
    }

    @Data
    public static class ConfiguracoesResponse {
        private String nome;
        private String email;
        private String mensagem;

        public ConfiguracoesResponse(String nome, String email, String mensagem) {
            this.nome = nome;
            this.email = email;
            this.mensagem = mensagem;
        }
    }

    @Data
    public static class ErrorResponse {
        private String erro;

        public ErrorResponse(String erro) {
            this.erro = erro;
        }
    }

}
