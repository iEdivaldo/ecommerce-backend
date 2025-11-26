package backend.ecommerce.ecommerce.servicos;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.ecommerce.ecommerce.entidades.Usuario;
import backend.ecommerce.ecommerce.repositorios.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioConfigService {
    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario atualizarConfiguracoes(String nome, String email, String senhaAtual, String novaSenha) {
        String emailLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepositorio.findByEmail(emailLogado)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + emailLogado));
        
        if (nome != null && !nome.trim().isEmpty()) {
            usuario.setNome(nome);
        }

        if (email != null && !email.trim().isEmpty() && !email.equals(usuario.getEmail())) {
            if (usuarioRepositorio.existsByEmail(email)) {
                throw new RuntimeException("O email já está em uso por outro usuário.");
            }
            usuario.setEmail(email);
        }

        if (novaSenha != null && !novaSenha.trim().isEmpty()) {
            if (senhaAtual == null || senhaAtual.trim().isEmpty()) {
                throw new RuntimeException("A senha atual é obrigatória para alterar a senha.");
            }
            if (!passwordEncoder.matches(senhaAtual, usuario.getSenhaHash())) {
                throw new RuntimeException("A senha atual está incorreta.");
            }
            usuario.setSenhaHash(passwordEncoder.encode(novaSenha));
        }

        return usuarioRepositorio.save(usuario);
    }
}
