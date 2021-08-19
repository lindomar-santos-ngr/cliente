package br.com.builders.clientes.service;

import br.com.builders.clientes.domain.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ClienteService {

    Page<Cliente> listarClientes(Pageable pageable);

    Cliente buscarClientePeloId(Long idCliente);

    Cliente salvarCliente(Cliente cliente);

    void excluirCliente(Long idCliente);

    Cliente editarCliente(Cliente cliente, Long idCliente);

    Cliente editarParcialCliente(Map<Object, Object> camposClientes, Long idCliente);
}
