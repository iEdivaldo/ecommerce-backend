package backend.ecommerce.ecommerce.servicos;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.ecommerce.ecommerce.autenticacao.dto.EnderecoResponse;
import backend.ecommerce.ecommerce.entidades.Endereco;
import backend.ecommerce.ecommerce.entidades.Usuario;
import backend.ecommerce.ecommerce.repositorios.EnderecoRepositorio;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoRepositorio enderecoRepositorio;

    public List<EnderecoResponse> listarEnderecosPorUsuario(Usuario usuario) {
        List<Endereco> enderecos = enderecoRepositorio.findByUsuarioId(usuario.getId());
        return enderecos.stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public Endereco criarEndereco(Endereco endereco, Usuario usuario) {
        endereco.setUsuario(usuario);
        return enderecoRepositorio.save(endereco);
    }

    @Transactional
    public Endereco atualizarEndereco(Long id, Endereco enderecoAtualizado, Usuario usuario) {
        Endereco enderecoExistente = enderecoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

        if (!enderecoExistente.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Acesso negado ao atualizar este endereço");
        }

        enderecoExistente.setLogradouro(enderecoAtualizado.getLogradouro());
        enderecoExistente.setNumero(enderecoAtualizado.getNumero());
        enderecoExistente.setComplemento(enderecoAtualizado.getComplemento());
        enderecoExistente.setBairro(enderecoAtualizado.getBairro());
        enderecoExistente.setCidade(enderecoAtualizado.getCidade());
        enderecoExistente.setEstado(enderecoAtualizado.getEstado());
        enderecoExistente.setCep(enderecoAtualizado.getCep());

        return enderecoRepositorio.save(enderecoExistente);
    }

    @Transactional
    public void deletarEndereco(Long id, Usuario usuario) {
        Endereco enderecoExistente = enderecoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

        if (!enderecoExistente.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Acesso negado ao deletar este endereço");
        }

        enderecoRepositorio.delete(enderecoExistente);
    }

    private EnderecoResponse converterParaResponse(Endereco endereco) {
        return EnderecoResponse.builder()
                .id(endereco.getId())
                .logradouro(endereco.getLogradouro())
                .numero(endereco.getNumero())
                .complemento(endereco.getComplemento())
                .bairro(endereco.getBairro())
                .cidade(endereco.getCidade())
                .estado(endereco.getEstado())
                .cep(endereco.getCep())
                .build();
    }

}
