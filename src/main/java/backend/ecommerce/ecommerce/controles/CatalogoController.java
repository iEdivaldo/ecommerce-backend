package backend.ecommerce.ecommerce.controles;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.ecommerce.ecommerce.entidades.Produto;
import backend.ecommerce.ecommerce.repositorios.ProdutoRepositorio;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class CatalogoController {

    private final ProdutoRepositorio produtoRepositorio;

    @GetMapping
    public List<Produto> listarProdutos() {
        return produtoRepositorio.findAll();
    }

    @GetMapping("/{id}")
    public Produto obterProdutoPorId(Long id) {
        return produtoRepositorio.findById(id).orElseThrow();
    }
}
