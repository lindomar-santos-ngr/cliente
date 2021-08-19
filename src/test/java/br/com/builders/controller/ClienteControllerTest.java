package br.com.builders.controller;

import br.com.builders.clientes.domain.Cliente;
import br.com.builders.clientes.service.ClienteService;
import br.com.builders.controller.error.RespostaErro;
import br.com.builders.controller.v1.ClienteDTO;
import br.com.builders.controller.v1.ClienteMapper;
import br.com.builders.exception.RecursoNaoEncontradoException;
import br.com.builders.exception.SemConteundoException;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClienteControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private ClienteController clienteController;

    @MockBean
    private ClienteService clienteService;

    @BeforeEach
    public void setup(){
        RestAssuredMockMvc.standaloneSetup(this.clienteController);
    }

    @Test
    public void deveRetornarStatus200AoListarClientes() {
        int size = 2;
        int page = 2;

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(String.format("http://localhost:%d/api/cliente/v1", port))
                .queryParam("size", size)
                .queryParam("page", page);

        HttpEntity<?> entity = getHttpEntity();

        Page<Cliente> listaClientePaginadas = new PageImpl<>(listarClientesMock(page));   //Mockito.mock(Page.class);
        Pageable pageable = PageRequest.of(page, size);
        when(clienteService.listarClientes(pageable)).thenReturn(listaClientePaginadas);

        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);

        Assert.assertEquals("Retorna status 200", 200, ((ResponseEntity) response).getStatusCodeValue());
    }

    @Test
    public void deveRetornarStatus204AoListarClientes() {
        int size = 2;
        int page = 2;

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(String.format("http://localhost:%d/api/cliente/v1", port))
                .queryParam("size", size)
                .queryParam("page", page);

        HttpEntity<?> entity = getHttpEntity();

        Page<Cliente> listaClientePaginadas = new PageImpl<>(listarClientesMock(0));   //Mockito.mock(Page.class);
        Pageable pageable = PageRequest.of(page, size);
        when(clienteService.listarClientes(pageable)).thenReturn(listaClientePaginadas);

        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);

        Assert.assertEquals(204, ((ResponseEntity) response).getStatusCodeValue());
    }


    @Test
    public void deveRetornarStatus200AobuscarClientePorIdExistente() {
        Long id = 2l;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(String.format("http://localhost:%d/api/cliente/v1/%s", port, id.toString()));

        HttpEntity<?> entity = getHttpEntity();

        Cliente cliente = getCliente(LocalDate.now(), 2);
        ClienteDTO clienteDTO = ClienteMapper.converterParaClienteDto(cliente);
        when(clienteService.buscarClientePeloId(id)).thenReturn(cliente);

        HttpEntity<ClienteDTO> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                ClienteDTO.class);

        verify(clienteService).buscarClientePeloId(id);

        Assert.assertEquals(200, ((ResponseEntity) response).getStatusCodeValue());
        Assert.assertEquals(clienteDTO.getNome(), response.getBody().getNome());
    }

    @Test
    public void deveRetornarStatus204AobuscarClientePorIdInexistente() {
        Long id = 2l;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(String.format("http://localhost:%d/api/cliente/v1/%s", port, id.toString()));

        HttpEntity<?> entity = getHttpEntity();

        when(clienteService.buscarClientePeloId(id)).thenThrow(RecursoNaoEncontradoException.class);

        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);

        verify(clienteService).buscarClientePeloId(id);

        Assert.assertEquals(404, ((ResponseEntity) response).getStatusCodeValue());
    }

    @Test
    public void deveRetornarStatus400AoSalvarClienteComDadosNulos() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(String.format("http://localhost:%d/api/cliente/v1/", port));

        Cliente cliente = getCliente(LocalDate.now(), 1);
        cliente.setNome(null);
        cliente.setId(null);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ClienteDTO> entity = new HttpEntity<>(ClienteMapper.converterParaClienteDto(cliente),  headers);

        when(clienteService.salvarCliente(cliente)).thenThrow(SemConteundoException.class);

        HttpEntity<RespostaErro> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                entity,
                RespostaErro.class);

        verify(clienteService).salvarCliente(cliente);

        Assert.assertEquals(400, ((ResponseEntity) response).getStatusCodeValue());
    }

    private HttpEntity<?> getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(headers);
    }

    private List<Cliente> listarClientesMock(int qtde){
        List<Cliente> listaCliente = new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        for (int i = 0; i < qtde; i++) {
            Cliente cliente = getCliente(localDate, i);
            listaCliente.add(cliente);
        }
        return listaCliente;
    }

    private Cliente getCliente(LocalDate localDate, int i) {
        Cliente cliente = new Cliente();
        cliente.setId((long) i);
        cliente.setNome("Teste " + i);
        cliente.setDataNascimento(localDate);
        cliente.setSexo(i % 2 == 0 ? "M" : "F");
        return cliente;
    }
}