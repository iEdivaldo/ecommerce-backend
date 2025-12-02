package backend.ecommerce.ecommerce.servicos;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import backend.ecommerce.ecommerce.autenticacao.dto.RegistrarRequest;
import backend.ecommerce.ecommerce.entidades.Usuario;
import backend.ecommerce.ecommerce.repositorios.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService  {

    private final UsuarioRepositorio usuarioRepositorio;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var usuario = usuarioRepositorio.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));
        
        var autoridades = new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name());

        return User.withUsername(usuario.getEmail())
            .password(usuario.getSenhaHash())
            .authorities(List.of(autoridades))
            .build();
    }

    public boolean existsByEmail(String email) {
        return usuarioRepositorio.existsByEmail(email);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.findAll();
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepositorio.findByEmail(email).orElse(null);
    }

    public Usuario obterUsuarioPorId(Long id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }

    public void salvarUsuario(RegistrarRequest request) {
        if (existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        var usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senhaHash(request.getSenha()) // A senha deve ser codificada no serviço
                .perfil(request.getPerfil())
                .build();
        usuarioRepositorio.save(usuario);
    }

    public void deletarUsuarioPorId(Long id) {
        usuarioRepositorio.deleteById(id);
    }
}
