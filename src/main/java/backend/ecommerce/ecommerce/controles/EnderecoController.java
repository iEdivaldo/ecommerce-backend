package backend.ecommerce.ecommerce.controles;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.ecommerce.ecommerce.autenticacao.dto.EnderecoRequest;
import backend.ecommerce.ecommerce.autenticacao.dto.EnderecoResponse;
import backend.ecommerce.ecommerce.servicos.EnderecoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/enderecos")
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoService enderecoService;

    @GetMapping
    public List<EnderecoResponse> listarEnderecoPorUsuario(@AuthenticationPrincipal UserDetails user) {
        return enderecoService.listarEnderecosPorUsuario(user.getUsername());
    }

    @PostMapping
    public EnderecoResponse criarEndereco(@AuthenticationPrincipal UserDetails user, @RequestBody EnderecoRequest req) {
        return enderecoService.criarEndereco(user.getUsername(), req);
    }

    @PutMapping("/{id}")
    public EnderecoResponse atualizarEndereco(@AuthenticationPrincipal UserDetails user, @PathVariable("id") Long id, @Valid @RequestBody EnderecoRequest req) {
        return enderecoService.atualizarEndereco(user.getUsername(), id, req);
    }

    @DeleteMapping("/{id}")
    public void deletarEndereco(@AuthenticationPrincipal UserDetails user, @PathVariable("id") Long id) {
        enderecoService.removerEndereco(user.getUsername(), id);
    }
}
