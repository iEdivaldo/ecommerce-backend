package backend.ecommerce.ecommerce.entidades;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "produtos")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = true)
    @ToString.Exclude
    private Categoria categoria;

    @Column(nullable = false)
    private String nomeProduto;

    @Column(nullable = false, unique = true)
    private String slugProduto;

    @Column(name = "descricao", columnDefinition = "text")
    private String descricaoProduto;

    @Column(name = "preco", nullable = false, precision = 12, scale = 2)
    private BigDecimal precoProduto;

    @Column(unique = true)
    private String codigoProduto;

    @Column(name = "estoque", nullable = false)
    @Builder.Default
    private Integer estoqueProduto = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean produtoAtivo = true;

    @Column(name = "imagem_url")
    private String imagemUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_criacao_id")
    @ToString.Exclude
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "senhaHash"})
    private Usuario usuarioCriacao;

}
