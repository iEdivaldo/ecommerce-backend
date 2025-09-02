package backend.ecommerce.ecommerce.autenticacao.dto;

import backend.ecommerce.ecommerce.entidades.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistrarRequest {

    @NotBlank
    private String nome;

    @Email @NotBlank
    private String email;

    @NotBlank
    private String senha;

    private Perfil perfil = Perfil.CLIENTE;

}
