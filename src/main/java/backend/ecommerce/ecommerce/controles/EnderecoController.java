package backend.ecommerce.ecommerce.controles;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.ecommerce.ecommerce.autenticacao.dto.EnderecoResponse;
import backend.ecommerce.ecommerce.entidades.Endereco;
import backend.ecommerce.ecommerce.entidades.Usuario;
import backend.ecommerce.ecommerce.repositorios.UsuarioRepositorio;
import backend.ecommerce.ecommerce.servicos.EnderecoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/enderecos")
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoService enderecoService;
    private final UsuarioRepositorio usuarioRepositorio;

    @GetMapping
    public ResponseEntity<List<EnderecoResponse>> listarEnderecos(Authentication authentication) {
        Usuario usuario = getUsuarioFromAuth(authentication);
        List<EnderecoResponse> enderecos = enderecoService.listarEnderecosPorUsuario(usuario);
        return ResponseEntity.ok(enderecos);
    }

    @PostMapping
    public ResponseEntity<Endereco> criarEndereco(@Valid @RequestBody Endereco endereco,
            Authentication authentication) {
        Usuario usuario = getUsuarioFromAuth(authentication);
        Endereco enderecoResponse = enderecoService.criarEndereco(endereco, usuario);
        return ResponseEntity.ok(enderecoResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Endereco> atualizarEndereco(@PathVariable("id") Long id, @Valid @RequestBody Endereco endereco,
            Authentication authentication) {
        Usuario usuario = getUsuarioFromAuth(authentication);
        Endereco enderecoAtualizado = enderecoService.atualizarEndereco(id, endereco, usuario);
        return ResponseEntity.ok(enderecoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEndereco(@PathVariable("id") Long id, Authentication authentication) {
        Usuario usuario = getUsuarioFromAuth(authentication);
        enderecoService.deletarEndereco(id, usuario);
        return ResponseEntity.noContent().build();
    }

    private Usuario getUsuarioFromAuth(Authentication authentication) {
        String email = authentication.getName();
        return usuarioRepositorio.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}
