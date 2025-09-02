package backend.ecommerce.ecommerce.autenticacao.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EnderecoRequest {

    @NotBlank
    private String logradouro;

    private String numero;

    private String complemento;

    private String bairro;

    @NotBlank
    private String cidade;

    @NotBlank
    private String estado;

    @NotBlank
    private String cep;

    @NotBlank
    private String pais;
    
    private boolean padrao = false;

}
