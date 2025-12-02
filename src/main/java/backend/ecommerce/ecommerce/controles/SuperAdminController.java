package backend.ecommerce.ecommerce.controles;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.ecommerce.ecommerce.entidades.Usuario;
import backend.ecommerce.ecommerce.servicos.UsuarioService;
import lombok.RequiredArgsConstructor;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/super_admin")
@RequiredArgsConstructor
public class SuperAdminController {

    private final UsuarioService usuarioService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/usuarios")
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/usuarios")
    public void criarUsuario(@RequestBody Usuario usuario) {
        usuarioService.salvarUsuario(usuario);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/usuario/{idUsuario}")
    public Usuario obterUsuarioPorId(@PathVariable("idUsuario") Long idUsuario) {
        return usuarioService.obterUsuarioPorId(idUsuario);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping("/usuario/{id}")
    public void atualizarUsuario(@PathVariable("id") Long id, @RequestBody Usuario usuario) {
        usuario.setId(id);
        usuarioService.salvarUsuario(usuario);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/usuario/{id}")
    public void deletarUsuario(@PathVariable("id") Long id) {
        usuarioService.deletarUsuarioPorId(id);
    }
    

}
