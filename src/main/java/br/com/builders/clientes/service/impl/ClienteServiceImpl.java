package br.com.builders.clientes.service.impl;

import br.com.builders.clientes.domain.Cliente;
import br.com.builders.clientes.repository.ClienteRepository;
import br.com.builders.clientes.service.ClienteService;
import br.com.builders.exception.RecursoNaoEncontradoException;
import br.com.builders.exception.SemConteundoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    private static final Logger LOG = LoggerFactory.getLogger(ClienteServiceImpl.class);

    @Autowired
    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Page<Cliente> listarClientes(Pageable pageable) {
        LOG.info("Listar Clientes  cadastrados");
        Page<Cliente> clientePage = this.clienteRepository.findAll(pageable);
        return clientePage;
    }

    @Override
    public Cliente buscarClientePeloId(Long idCliente) {
        LOG.info("Buscar cliente {}", idCliente);
        Optional<Cliente> clienteOptional = this.clienteRepository.findById(idCliente);
        if(clienteOptional == null || !clienteOptional.isPresent()){
            throw new RecursoNaoEncontradoException("Nao ha cliente com este id " + idCliente);
        }
        return clienteOptional.get();
    }

    @Override
    public Cliente salvarCliente(Cliente cliente) {
        LOG.info("Salvar cliente {}", cliente);
        Cliente cliente1 = null;
        try {
            cliente1 = this.clienteRepository.save(cliente);
        }catch (DataIntegrityViolationException e){
            throw new SemConteundoException("Dados do cliente invalido, verifique e tente novamente");
        }
        return cliente1;
    }

    @Override
    public void excluirCliente(Long idCliente) {
        LOG.info("Excluir cliente pelo id {} ", idCliente);
        this.clienteRepository.deleteById(idCliente);
    }

    @Override
    public Cliente editarCliente(Cliente cliente, Long id) {
        LOG.info("Editar cliente {} ", cliente);
        Cliente clienteSelecionado = this.buscarClientePeloId(id);

        if(existeCliente(clienteSelecionado)){
            cliente.setId(id);
            clienteSelecionado = this.salvarCliente(cliente);
        }
        return clienteSelecionado;
    }

    @Override
    public Cliente editarParcialCliente(Map<Object, Object> camposClientes, Long id) {
        Cliente cliente = buscarClientePeloId(id);

        Cliente clienteAlterado = null;
        if(existeCliente(cliente)){
            camposClientes.forEach((chave, valor) -> {
                Field campoAlterado = ReflectionUtils.findField(Cliente.class, (String) chave);
                campoAlterado.setAccessible(true);
                ReflectionUtils.setField(campoAlterado, cliente, valor);
            });

            clienteAlterado = cliente;
            this.salvarCliente(clienteAlterado);
        }
        return clienteAlterado;
    }

    private boolean existeCliente(Cliente cliente){
        return cliente != null;
    }

}
