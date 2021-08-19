package br.com.builders.controller;

import br.com.builders.clientes.domain.Cliente;
import br.com.builders.clientes.service.ClienteService;
import br.com.builders.controller.error.RespostaErro;
import br.com.builders.controller.v1.ClienteDTO;
import br.com.builders.controller.v1.ClienteMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Api(tags = "End point Cliente")
@RestController
@RequestMapping("api/cliente/v1")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @ApiOperation(value = "Listar clientes", nickname = "Listar", response = ClienteDTO[].class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso", response = ClienteDTO[].class),
            @ApiResponse(code = 204, message = "Nao ha clientes cadastrados", response = Object.class)
    })
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity listarClientes(Pageable pageable) {
        List<ClienteDTO> clientesDtos = ClienteMapper.converterParaClientesDTO(clienteService.listarClientes(pageable).getContent());
        return clientesDtos == null || clientesDtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(clientesDtos);
    }

    @ApiOperation(value = "Buscar cliente  pelo id", nickname = "Buscar", response = ClienteDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso", response = ClienteDTO.class),
            @ApiResponse(code = 204, message = "No content", response = Object.class),
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity buscarClientePorId(@ApiParam(value = "id", name = "idCliente", example = "12", required = true) @PathVariable(value = "id") Long id){
       Cliente cliente = clienteService.buscarClientePeloId(id);
       return cliente != null ? ResponseEntity.ok(ClienteMapper.converterParaClienteDto(cliente)) : ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Adicionar cliente", nickname = "Adicionar")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso", response = ClienteDTO.class),
            @ApiResponse(code = 201, message = "Adicionado com sucesso", response = Object.class),
            @ApiResponse(code = 400, message = "Dados clientes invalidos", response = RespostaErro.class),
    })
    @PostMapping
    public ResponseEntity adicionarCliente(@ApiParam(value = "cliente", name = "cliente", required = true)  @RequestBody ClienteDTO clienteDto) {
        Cliente cliente = clienteService.salvarCliente(ClienteMapper.converterParaCliente(clienteDto));
        URI location = getUri(cliente.getId());
        return ResponseEntity.created(location).build();
    }

    @ApiOperation(value = "Excluir cliente", nickname = "Excluir")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso na exclusao"),
            @ApiResponse(code = 204, message = "Nao ha clientes com esse id", response = Object.class)
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity deletarCliente(@ApiParam(value = "id", name = "idCliente", example = "12", required = true) @PathVariable(value = "id") Long id){
        clienteService.excluirCliente(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Editar cliente", nickname = "Editar")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso", response = ClienteDTO.class),
            @ApiResponse(code = 204, message = "Não há cliente com esse id", response = Object.class),
            @ApiResponse(code = 400, message = "Dados clientes invalidos", response = RespostaErro.class)
    })
    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity editarCliente(@ApiParam(value = "id", name = "idCliente", example = "12", required = true) @PathVariable(value = "id") Long id,
                                        @ApiParam(value = "cliente", name = "cliente", required = true) @RequestBody ClienteDTO clienteDTO){
        ClienteDTO cliente = ClienteMapper.converterParaClienteDto(clienteService.editarCliente(ClienteMapper.converterParaCliente(clienteDTO), id));
        return ResponseEntity.ok(cliente);
    }

    @ApiOperation(value = "Editar dados especificos do cliente", nickname = "Editar Campo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso"),
            @ApiResponse(code = 204, message = "Edicao bem sucessida", response = String.class),
            @ApiResponse(code = 400, message = "Dados clientes invalidos", response = RespostaErro.class)
    })
    @PatchMapping(value = "/{id}")
    public ResponseEntity editarInformacaoCliente(@ApiParam(value = "id", name = "idCliente", example = "12",required = true) @PathVariable(value = "id") Long id,
                                                  @ApiParam(value = "atributo cliente", name = "atributo cliente", required = true) @RequestBody Map<Object, Object> camposCliente){

        ClienteDTO cliente = ClienteMapper.converterParaClienteDto(clienteService.editarParcialCliente(camposCliente, id));
        return ResponseEntity.ok(cliente);
    }

    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }

}
