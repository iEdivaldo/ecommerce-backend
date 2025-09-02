package backend.ecommerce.ecommerce.entidades;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Entity @Table(name = "produtos")
@Data @AllArgsConstructor @Builder
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    @ToString.Exclude
    private Categoria categoria;

    @Column(nullable = false)
    private String nomeProduto;

    @Column(nullable = false, unique = true)
    private String slugProduto;

    @Column(columnDefinition = "text")
    private String descricaoProduto;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precoProduto;

    @Column(unique = true)
    private String codigoProduto;

    @Column(nullable = false)
    @Builder.Default
    private Integer estoqueProduto = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean produtoAtivo = true;

}
