package backend.ecommerce.ecommerce.servicos;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.ecommerce.ecommerce.autenticacao.dto.EnderecoRequest;
import backend.ecommerce.ecommerce.autenticacao.dto.EnderecoResponse;
import backend.ecommerce.ecommerce.entidades.Endereco;
import backend.ecommerce.ecommerce.entidades.Usuario;
import backend.ecommerce.ecommerce.repositorios.EnderecoRepositorio;
import backend.ecommerce.ecommerce.repositorios.UsuarioRepositorio;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoRepositorio enderecoRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;

    @Transactional(readOnly = true)
    public List<EnderecoResponse> listarEnderecosPorUsuario(String email) {
        var usuario = usuarioRepositorio.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
            return enderecoRepositorio.findByUsuario(usuario.getId()).stream().map(this::toResp).toList();
    }

    @Transactional
    public EnderecoResponse criarEndereco(String email, EnderecoRequest req) {
        var usuario = usuarioRepositorio.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
        
        if (Boolean.TRUE.equals(req.isPadrao())){
            enderecoRepositorio.desmarcarEnderecoPadrao(usuario.getId());
        }

        var endereco = Endereco.builder()
        .usuario(usuario)
        .logradouro(req.getLogradouro())
        .numero(req.getNumero())
        .complemento(req.getComplemento())
        .bairro(req.getBairro())
        .cidade(req.getCidade())
        .estado(req.getEstado())
        .cep(req.getCep())
        .pais(req.getPais())
        .padrao(req.isPadrao())
        .build();

        return toResp(enderecoRepositorio.save(endereco));
    }

    @Transactional
    public EnderecoResponse atualizarEndereco(String email, Long id, EnderecoRequest req) {
        var endereco = buscarDoUsuarioOuError(email, id);

        if (Boolean.TRUE.equals(req.isPadrao())){
            enderecoRepositorio.desmarcarEnderecoPadrao(endereco.getUsuario().getId());
            endereco.setPadrao(true);
        } else {
            endereco.setPadrao(false);
        }

        endereco.setLogradouro(req.getLogradouro());
        endereco.setNumero(req.getNumero());
        endereco.setComplemento(req.getComplemento());
        endereco.setBairro(req.getBairro());
        endereco.setCidade(req.getCidade());
        endereco.setEstado(req.getEstado());
        endereco.setCep(req.getCep());
        endereco.setPais(req.getPais());

        return toResp(endereco);
    }

    @Transactional
    public void removerEndereco(String email, Long id) {
        var endereco = buscarDoUsuarioOuError(email, id);
        enderecoRepositorio.delete(endereco);
    }

    private Endereco buscarDoUsuarioOuError(String email, Long id) {
        Usuario usuario = usuarioRepositorio.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        return enderecoRepositorio.findById(id).filter(endereco -> endereco.getUsuario().getId().equals(usuario.getId())).orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado para o usuário"));
    }

    private EnderecoResponse toResp (Endereco endereco) {
        return new EnderecoResponse(endereco.getId(), endereco.getLogradouro(), endereco.getNumero(), endereco.getComplemento(), endereco.getBairro(),
        endereco.getCidade(), endereco.getEstado(), endereco.getCep(), endereco.getPais(), endereco.getUsuario().getId(), endereco.getPadrao());
    }

}
