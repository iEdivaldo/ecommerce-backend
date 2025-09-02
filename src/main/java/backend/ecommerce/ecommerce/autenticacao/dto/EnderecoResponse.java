package backend.ecommerce.ecommerce.autenticacao.dto;

public record EnderecoResponse(
    Long id,
    String logradouro,
    String numero,
    String complemento,
    String bairro,
    String cidade,
    String estado,
    String cep,
    String pais,
    Long usuarioId,
    boolean padrao
) {}
