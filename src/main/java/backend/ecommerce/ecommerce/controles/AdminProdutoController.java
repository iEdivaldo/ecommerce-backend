package backend.ecommerce.ecommerce.controles;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.ecommerce.ecommerce.entidades.Produto;
import backend.ecommerce.ecommerce.repositorios.ProdutoRepositorio;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/administracao/produtos")
@RequiredArgsConstructor
public class AdminProdutoController {

    private final ProdutoRepositorio produtoRepositorio;

    @PreAuthorize("hasRole('ADMINISTRACAO')")
    @PostMapping
    public void criarProduto(@RequestBody Produto produto) {
        produtoRepositorio.save(produto);
    }

    @PreAuthorize("hasRole('ADMINISTRACAO')")
    @PostMapping("/{id}")
    public void atualizarProduto(@PathVariable Long id, @RequestBody Produto produto) {
        produto.setId(id);
        // atualizar informacao como verificação se existe esse produto ou não...
        produtoRepositorio.save(produto);
    }

    @PreAuthorize("hasRole('ADMINISTRACAO')")
    @DeleteMapping("/{id}")
    public void deletarProduto(@PathVariable Long id) {
        produtoRepositorio.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMINISTRACAO')")
    @GetMapping
    public List<Produto> listarProdutos() {
        return produtoRepositorio.findAll();
    }

}
