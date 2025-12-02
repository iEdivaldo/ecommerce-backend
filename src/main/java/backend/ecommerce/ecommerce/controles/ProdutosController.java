package backend.ecommerce.ecommerce.controles;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.ecommerce.ecommerce.entidades.Produto;
import backend.ecommerce.ecommerce.repositorios.ProdutoRepositorio;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutosController {

    private final ProdutoRepositorio produtoRepositorio;

    @GetMapping
    public List<Produto> listarProdutos() {
        return produtoRepositorio.findAll();
    }

    @GetMapping("/{idProdutos}")
    public Produto obterProdutoPorId(@PathVariable("idProdutos") Long idProduto) {
        return produtoRepositorio.findById(idProduto).orElseThrow();
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Produto>> listarProdutosPorCategoria(@PathVariable("categoriaId") Long categoriaId) {
        List<Produto> produtos = produtoRepositorio.findByCategoriaId(categoriaId);
        return ResponseEntity.ok(produtos);
    }
}
