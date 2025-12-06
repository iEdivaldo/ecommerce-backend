package backend.ecommerce.ecommerce.entidades.dto;

import java.util.List;

import backend.ecommerce.ecommerce.entidades.MetodoPagamento;
import lombok.Data;

@Data
public class CriarPedidoDTO {

    private Long enderecoId;
    private MetodoPagamento metodoPagamento;
    private List<ItemPedidoDTO> itens;
}
