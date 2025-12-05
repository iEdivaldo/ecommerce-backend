package backend.ecommerce.ecommerce.controles;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.ecommerce.ecommerce.entidades.Categoria;
import backend.ecommerce.ecommerce.entidades.Produto;
import backend.ecommerce.ecommerce.entidades.Usuario;
import backend.ecommerce.ecommerce.repositorios.CategoriaRepositorio;
import backend.ecommerce.ecommerce.repositorios.ProdutoRepositorio;
import backend.ecommerce.ecommerce.servicos.UsuarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/administracao")
@RequiredArgsConstructor
public class AdminProdutoController {

    private final ProdutoRepositorio produtoRepositorio;
    private final CategoriaRepositorio categoriaRepositorio;
    private final UsuarioService usuarioService;

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/produtos")
    public void criarProduto(@RequestBody Produto produto) {
        String emailLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioService.buscarPorEmail(emailLogado);

        produto.setUsuarioCriacao(usuario);
        produtoRepositorio.save(produto);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMINISTRADOR')")
    @GetMapping("/produtos/usuario/{usuarioId}")
    public List<Produto> listarProdutosPorUsuario(@PathVariable("usuarioId") Long usuarioId) {
        return produtoRepositorio.findByUsuarioCriacaoId(usuarioId);
    }
 
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/produtos/{id}")
    public void atualizarProduto(@PathVariable("id") Long id, @RequestBody Produto produto) {
        produto.setId(id);
        // atualizar informacao como verificação se existe esse produto ou não...
        produtoRepositorio.save(produto);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMIN')")
    @DeleteMapping("/produtos/{id}")
    public void deletarProduto(@PathVariable("id") Long id) {
        produtoRepositorio.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('SUPER_ADMIN')")
    @GetMapping("/produtos")
    public List<Produto> listarProdutos() {
        return produtoRepositorio.findAll();
    }

    @GetMapping("/categorias")
    public List<Categoria> listarCategorias() {
        return categoriaRepositorio.findAll();
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/categorias")
    public void criarCategoria(@RequestBody Categoria categoria) {
        categoriaRepositorio.save(categoria);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping("/categorias/{id}")
    public void atualizarCategoria(@PathVariable("id") Long id, @RequestBody Categoria categoria) {
        categoria.setId(id);
        categoriaRepositorio.save(categoria);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/categorias/{id}")
    @Transactional
    public void deletarCategoria(@PathVariable("id") Long id) {
        Categoria categoria = categoriaRepositorio.findById(id).orElseThrow();
        List<Produto> produtos = produtoRepositorio.findByCategoriaId(categoria.getId());
        for (Produto produto : produtos) {
            produto.setCategoria(null);
            produtoRepositorio.saveAndFlush(produto);
        }
        categoriaRepositorio.deleteById(id);
    }

}
