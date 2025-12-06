package backend.ecommerce.ecommerce.servicos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import backend.ecommerce.ecommerce.entidades.Endereco;
import backend.ecommerce.ecommerce.entidades.ItemPedido;
import backend.ecommerce.ecommerce.entidades.Notificacao;
import backend.ecommerce.ecommerce.entidades.Pedido;
import backend.ecommerce.ecommerce.entidades.Produto;
import backend.ecommerce.ecommerce.entidades.StatusPedido;
import backend.ecommerce.ecommerce.entidades.TipoNotificacao;
import backend.ecommerce.ecommerce.entidades.Usuario;
import backend.ecommerce.ecommerce.entidades.dto.CriarPedidoDTO;
import backend.ecommerce.ecommerce.repositorios.EnderecoRepositorio;
import backend.ecommerce.ecommerce.repositorios.NotificacaoRepositorio;
import backend.ecommerce.ecommerce.repositorios.PedidoRepositorio;
import backend.ecommerce.ecommerce.repositorios.ProdutoRepositorio;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepositorio pedidoRepositorio;
    private final ProdutoRepositorio produtoRepositorio;
    private final EnderecoRepositorio enderecoRepositorio;
    private final NotificacaoRepositorio notificacaoRepositorio;

    @Transactional
    public Pedido criarPedido(CriarPedidoDTO dto, Usuario cliente) {
        // Validar o endereço
        Endereco endereco = enderecoRepositorio.findById(dto.getEnderecoId())
            .orElseThrow(() -> new IllegalArgumentException("Endereço inválido"));

        if (!endereco.getUsuario().getId().equals(cliente.getId())) {
            throw new IllegalArgumentException("Endereço não pertence ao cliente");
        }

        // Criar o pedido
        Pedido pedido = Pedido.builder()
            .cliente(cliente)
            .endereco(endereco)
            .metodoPagamento(dto.getMetodoPagamento())
            .status(StatusPedido.CRIADO)
            .subTotal(BigDecimal.ZERO)
            .frete(BigDecimal.valueOf(5.00))
            .total(BigDecimal.ZERO)
            .itens(new ArrayList<>())
            .build();

        BigDecimal subTotal = BigDecimal.ZERO;
        Set<Long> vendedoresNotificar = new HashSet<>();

        // Adicionar os itens no pedido
        for (var itemDTO : dto.getItens()) {
            Produto produto = produtoRepositorio.findById(itemDTO.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + itemDTO.getProdutoId()));

            // validar o estoque do produto
            if (produto.getEstoqueProduto() < itemDTO.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para: " + produto.getNomeProduto());
            }


            // Reduzir o estoque do produto
            produto.setEstoqueProduto(produto.getEstoqueProduto() - itemDTO.getQuantidade());
            produtoRepositorio.save(produto);

            // Cria item do pedido
            ItemPedido item = ItemPedido.builder()
                .pedido(pedido)
                .produto(produto)
                .quantidade(itemDTO.getQuantidade())
                .precoUnitario(produto.getPrecoProduto())
                .build();

            pedido.getItens().add(item);

            BigDecimal subTotalItem = produto.getPrecoProduto()
                .multiply(BigDecimal.valueOf(itemDTO.getQuantidade()));
            subTotal = subTotal.add(subTotalItem);

            // Adiciona vendedor para notificar
            if (produto.getUsuarioCriacao() != null) {
                vendedoresNotificar.add(produto.getUsuarioCriacao().getId());
            }            
        }

        pedido.setSubTotal(subTotal);
        pedido.setTotal(subTotal.add(pedido.getFrete()));

        Pedido pedidoSalvo = pedidoRepositorio.save(pedido);

        // Notificar os vendedores do pedido
        for (Long vendedorId: vendedoresNotificar) {
            Usuario vendedor = new Usuario();
            vendedor.setId(vendedorId);

            Notificacao notificacao = new Notificacao();
            notificacao.setUsuario(vendedor);
            notificacao.setTitulo("Novo pedido #" + pedidoSalvo.getId());
            notificacao.setMensagem("Você recebeu um novo pedido de vendas de " + cliente.getNome() + ".");
            notificacao.setTipo(TipoNotificacao.PEDIDO_NOVO);
            notificacao.setLida(false);
            notificacao.setDataHora(LocalDateTime.now());

            notificacaoRepositorio.save(notificacao);
        }

        return pedidoSalvo;
    }

    public List<Pedido> listarPedidosPorCliente(Long clienteId) {
        return pedidoRepositorio.findByClienteIdOrderByCriadoEmDesc(clienteId);
    }

    public List<Pedido> listarPedidosVendedor(Long vendedorId) {
        List<Pedido> pedidos = pedidoRepositorio.findPedidosPorVendedor(vendedorId);
        System.out.println("==== VENDAS ENCONTRADAS: " + pedidos.size());
        pedidos.forEach(p -> {
            System.out.println("Pedido ID: " + p.getId());
            System.out.println("Itens: " + p.getItens().size());
            p.getItens().forEach(item -> {
                System.out.println("  - Produto: " + item.getProduto().getNomeProduto());
            });
        });
        return pedidos;
    }

    public Pedido obterPedidoPorId(Long pedidoId, Long usuarioId) {
        Pedido pedido = pedidoRepositorio.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        
        // Verificando se o usuario é o cliente ou o vendedor do pedido
        boolean isCliente = pedido.getCliente().getId().equals(usuarioId);
        boolean isVendedor = pedido.getItens().stream()
            .anyMatch(item -> item.getProduto().getUsuarioCriacao() != null &&
                              item.getProduto().getUsuarioCriacao().getId().equals(usuarioId));

        if (!isCliente && !isVendedor) {
            throw new RuntimeException("Acesso negado ao pedido");
        }
        
        return pedido;
    }

    @Transactional
    public void atualizarStatusPedido(Long pedidoId, StatusPedido novoStatus, Long vendedorId) {
        Pedido pedido = pedidoRepositorio.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        // Verificar seo vendedor tem produtos neste pedido
        boolean hasProdutosDoVendedor = pedido.getItens().stream()
            .anyMatch(item -> item.getProduto().getUsuarioCriacao() != null &&
                              item.getProduto().getUsuarioCriacao().getId().equals(vendedorId));

        if (!hasProdutosDoVendedor) {
            throw new RuntimeException("Vendedor não autorizado a atualizar este pedido");
        }

        StatusPedido statusAnterior = pedido.getStatus();
        pedido.setStatus(novoStatus);

        switch (novoStatus) {
            case PAGO -> pedido.setPagoEm(LocalDateTime.now());
            case ENVIADO -> pedido.setEnviadoEm(LocalDateTime.now());
            case ENTREGUE -> pedido.setEntregueEm(LocalDateTime.now());
            case CANCELADO -> {
                pedido.setCanceladoEm(LocalDateTime.now());
                // Restaurar o estoque dos produtos
                for (ItemPedido item : pedido.getItens()) {
                    Produto produto = item.getProduto();
                    produto.setEstoqueProduto(produto.getEstoqueProduto() + item.getQuantidade());
                    produtoRepositorio.save(produto);
                }
            }
            default -> {}
        }

        pedidoRepositorio.save(pedido);

        // Notificar o cliente sobre a atualização do status
        Notificacao notificacao = new Notificacao();
        notificacao.setUsuario(pedido.getCliente());
        notificacao.setTitulo("Atualização do pedido #" + pedido.getId());
        notificacao.setMensagem(getMensagemStatus(novoStatus, statusAnterior));
        notificacao.setTipo(TipoNotificacao.PEDIDO_ATUALIZADO);
        notificacao.setLida(false);
        notificacao.setDataHora(LocalDateTime.now());
        notificacaoRepositorio.save(notificacao);
    }

    private String getMensagemStatus(StatusPedido novoStatus, StatusPedido statusAnterior) {
        return switch (novoStatus) {
            case PAGO -> "O pagamento foi confirmado!";
            case ENVIADO -> "Seu pedido foi enviado!";
            case ENTREGUE -> "Seu pedido foi entregue!";
            case CANCELADO -> "Seu pedido foi cancelado!";
            default -> "O status do seu pedido foi atualizado de " + statusAnterior + " para: " + novoStatus;
        };
    }
}