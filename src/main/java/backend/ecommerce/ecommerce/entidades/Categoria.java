package backend.ecommerce.ecommerce.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity @Table(name = "categorias")
@Data @AllArgsConstructor 
@Builder @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true) @NoArgsConstructor
public class Categoria {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    @ToString.Include
    private String nome;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    @Builder.Default
    private Boolean produtoAtivo = true;
}
