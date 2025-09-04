package backend.ecommerce.ecommerce.autenticacao;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.ecommerce.ecommerce.autenticacao.dto.LoginRequest;
import backend.ecommerce.ecommerce.autenticacao.dto.RegistrarRequest;
import backend.ecommerce.ecommerce.autenticacao.dto.TokenResponse;
import backend.ecommerce.ecommerce.entidades.Usuario;
import backend.ecommerce.ecommerce.repositorios.UsuarioRepositorio;
import backend.ecommerce.ecommerce.servicos.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/autenticacao")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/registrar")
    public Map<String, Object> registrar (@Valid @RequestBody RegistrarRequest request) {
        if (usuarioRepositorio.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        var usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senhaHash(passwordEncoder.encode(request.getSenha()))
                .perfil(request.getPerfil())
                .build();

        usuarioRepositorio.save(usuario);

        var acesso = tokenService.gerarAccessoToken(usuario.getEmail(), usuario.getPerfil().name());
        var refresh = tokenService.gerarRefreshToken(usuario.getEmail());

        return Map.of(
            "usuario", Map.of("id", usuario.getId(), "nome", usuario.getNome(), "email", usuario.getEmail(), "perfil", usuario.getPerfil()),
            "tokens", new TokenResponse(acesso, refresh)
        );
    }

    @PostMapping("/login")
    public Map<String, Object> login(@Valid @RequestBody LoginRequest request) {
        var autenticacaoToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha());
        authenticationManager.authenticate(autenticacaoToken);

        var usuario = usuarioRepositorio.findByEmail(request.getEmail()).orElseThrow();
        var acesso = tokenService.gerarAccessoToken(usuario.getEmail(), usuario.getPerfil().name());
        var refresh = tokenService.gerarRefreshToken(usuario.getEmail());

        return Map.of(
            "usuario", Map.of("id", usuario.getId(), "nome", usuario.getNome(), "email", usuario.getEmail(), "perfil", usuario.getPerfil()),
            "tokens", new TokenResponse(acesso, refresh)
        );
        
    }

    @GetMapping("/logout")
    public Map<String, Object> logout() {
        // Implementar lógica de logout, se necessário
        return Map.of(
            "message", "Logout realizado com sucesso",
            "tokens", new TokenResponse("", "")
        );
    }

    @PostMapping("/atualizar-token")
    public TokenResponse atualizarToken(@RequestBody Map<String, String> body) {
        var refresh = body.getOrDefault("tokenAtualizacao", "");
        var claims = tokenService.validarToken(refresh).getBody();

        if(!"refresh".equals(claims.get("tipo")))  throw new RuntimeException("Token inválido");
        
        var email = claims.getSubject();
        var usuario = usuarioRepositorio.findByEmail(email).orElseThrow();
        var acesso = tokenService.gerarAccessoToken(usuario.getEmail(), usuario.getPerfil().name());

        return new TokenResponse(acesso, refresh);
    }
    
}
