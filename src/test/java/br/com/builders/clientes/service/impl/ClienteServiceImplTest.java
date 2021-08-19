package br.com.builders.clientes.service.impl;

import br.com.builders.clientes.domain.Cliente;
import br.com.builders.clientes.repository.ClienteRepository;
import static org.hamcrest.Matchers.equalTo;

import br.com.builders.exception.RecursoNaoEncontradoException;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ErrorCollector;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ClienteServiceImplTest {

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private Pageable pageableMock;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        clienteService = new ClienteServiceImpl(clienteRepository);
    }

    @Test
    void listarClientes() {
        Page<Cliente> listaClientePaginadas = Mockito.mock(Page.class);

        Pageable pageable = PageRequest.of(0, 3);

        when(clienteRepository.findAll(pageable)).thenReturn(listaClientePaginadas);

        this.clienteService.listarClientes(pageable);

        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(this.clienteRepository).findAll(pageableArgumentCaptor.capture());

        error.checkThat(pageableArgumentCaptor.getValue().getPageSize(), equalTo(3));
    }

    @Test
    void buscarClientePeloId() {
        Cliente cliente = getCliente(LocalDate.now(), 2);
        Optional<Cliente> optionalCliente = Optional.of(cliente);

        when(clienteRepository.findById(cliente.getId())).thenReturn(optionalCliente);

        Cliente clienteSelecionado = clienteService.buscarClientePeloId(cliente.getId());

        ArgumentCaptor<Long> uuidArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        verify(this.clienteRepository).findById(uuidArgumentCaptor.capture());

        error.checkThat(uuidArgumentCaptor.getValue(), equalTo(clienteSelecionado.getId()));
    }

    @Test
    void buscarClientePeloIdInexistente() {
        Optional<Cliente> optionalCliente = Optional.ofNullable(null);
        Long idClienteInexistente = 1l;

        when(clienteRepository.findById(null)).thenThrow(RecursoNaoEncontradoException.class);

        try {
            clienteService.buscarClientePeloId(idClienteInexistente);
        }catch (RecursoNaoEncontradoException e){
            error.checkThat(e.getMessage(), equalTo("Nao ha cliente com este id 1"));
        }
    }

    @Test
    void salvarCliente() {
        Cliente cliente = getCliente(LocalDate.now(), 1);

        when(clienteRepository.save(Mockito.any(Cliente.class))).thenReturn(cliente);

        Cliente clienteDtoComId = clienteService.salvarCliente(cliente);

        ArgumentCaptor<Cliente> clienteArgumentCaptor = ArgumentCaptor.forClass(Cliente.class);

        verify(this.clienteRepository).save(clienteArgumentCaptor.capture());

        error.checkThat(clienteArgumentCaptor.getValue().getId(), equalTo(clienteDtoComId.getId()));
    }

    @Test
    void excluirCliente() {

        Long id = 1l;

        doNothing().when(clienteRepository).deleteById(id);

        clienteService.excluirCliente(id);

        ArgumentCaptor<Long> uuidArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        verify(clienteRepository).deleteById(uuidArgumentCaptor.capture());

        error.checkThat(uuidArgumentCaptor.getValue(), equalTo(id));
    }

    @Test
    void editarCliente() {

        Cliente cliente = getCliente(LocalDate.now(), 1);
        Optional<Cliente> clienteOptional = Optional.of(cliente);

        when(clienteRepository.findById(cliente.getId())).thenReturn(clienteOptional);
        when(clienteRepository.save(Mockito.any(Cliente.class))).thenReturn(cliente);

        Cliente clienteDtoComId = clienteService.editarCliente(cliente, cliente.getId());

        ArgumentCaptor<Cliente> clienteArgumentCaptor = ArgumentCaptor.forClass(Cliente.class);

        verify(this.clienteRepository).save(clienteArgumentCaptor.capture());

        error.checkThat(clienteArgumentCaptor.getValue().getId(), equalTo(clienteDtoComId.getId()));

    }

    @Test
    void edicaoParcialDosDadosDoCliente() {

        Cliente cliente = getCliente(LocalDate.now(), 1);
        Optional<Cliente> clienteOptional = Optional.of(cliente);

        Map<Object, Object> camposCliente = new HashMap<>();
        camposCliente.put("nome", "novo nome");
        camposCliente.put("sexo", "M");

        Cliente clienteAlterado = new Cliente();
        clienteAlterado.setId(cliente.getId());
        clienteAlterado.setDataNascimento(cliente.getDataNascimento());
        clienteAlterado.setSexo("M");
        clienteAlterado.setNome("novo nome");

        when(clienteRepository.findById(cliente.getId())).thenReturn(clienteOptional);
        when(clienteRepository.save(Mockito.any(Cliente.class))).thenReturn(clienteAlterado);

        Cliente clienteDtoAlterado = clienteService.editarParcialCliente(camposCliente, cliente.getId());

        ArgumentCaptor<Cliente> clienteArgumentCaptor = ArgumentCaptor.forClass(Cliente.class);

        verify(this.clienteRepository).save(clienteArgumentCaptor.capture());

        error.checkThat(clienteArgumentCaptor.getValue().getId(), equalTo(clienteDtoAlterado.getId()));
        error.checkThat(clienteArgumentCaptor.getValue().getSexo(), equalTo(clienteDtoAlterado.getSexo()));
        error.checkThat(clienteArgumentCaptor.getValue().getNome(), equalTo(clienteDtoAlterado.getNome()));

    }

    private List<Cliente> listarClientesMock(){
        List<Cliente> listaCliente = new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        for (int i = 0; i < 5; i++) {
            Cliente cliente = getCliente(localDate, i);
            listaCliente.add(cliente);
        }
        return listaCliente;
    }

    private Cliente getCliente(LocalDate localDate, int i) {
        Cliente cliente = new Cliente();
        cliente.setId((long) i);
        cliente.setDataNascimento(localDate);
        cliente.setSexo(i % 2 == 0 ? "M" : "F");
        return cliente;
    }
}