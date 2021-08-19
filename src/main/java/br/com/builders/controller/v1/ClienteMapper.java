package br.com.builders.controller.v1;

import br.com.builders.clientes.domain.Cliente;

import java.util.List;
import java.util.stream.Collectors;

public class ClienteMapper {

    public static ClienteDTO converterParaClienteDto(Cliente cliente){
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setSexo(cliente.getSexo());
        clienteDTO.setDataNascimento(cliente.getDataNascimento());
        clienteDTO.setNome(cliente.getNome());
        clienteDTO.setId(cliente.getId());
        return clienteDTO;
    }

    public static Cliente converterParaCliente(ClienteDTO clienteDTO){
        Cliente cliente = new Cliente();
        cliente.setSexo(clienteDTO.getSexo());
        cliente.setDataNascimento(clienteDTO.getDataNascimento());
        cliente.setNome(clienteDTO.getNome());
        cliente.setId(clienteDTO.getId());
        return cliente;
    }

    public static List<ClienteDTO> converterParaClientesDTO(List<Cliente> listaClientes){
        List<ClienteDTO> listaClientesDtos = null;
        if(listaClientes != null && !listaClientes.isEmpty()){
            listaClientesDtos = listaClientes.stream()
                    .map(ClienteMapper::converterParaClienteDto)
                    .collect(Collectors.toList());
        }
        return listaClientesDtos;
    }
}
