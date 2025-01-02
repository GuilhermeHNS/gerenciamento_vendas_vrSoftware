import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.ClienteDAO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.RegisterUpdateClienteRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.exceptions.ClienteNotFoundException;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.exceptions.ValidationException;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Cliente;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.ClienteService;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.impl.ClienteServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class ClienteServiceTest {
    private ClienteDAO clienteDAO;
    private ClienteService clienteService;

    @BeforeEach
    public void setUp() {
        clienteDAO = Mockito.mock(ClienteDAO.class);
        clienteService = new ClienteServiceImpl(clienteDAO);
    }

    @Test
    public void deveRegistrarUmCliente() throws SQLException {
        RegisterUpdateClienteRequest request = new RegisterUpdateClienteRequest("Guilherme", "444.444.444-00", "500.00", "10", true);

        Mockito.doNothing().when(clienteDAO).createCliente(Mockito.any(Cliente.class));

        Assertions.assertDoesNotThrow(() -> clienteService.createCliente(request));

        verify(clienteDAO, times(1)).createCliente(Mockito.any(Cliente.class));
    }

    @Test
    public void deveNegarCadastroSemNome() {
        RegisterUpdateClienteRequest request = new RegisterUpdateClienteRequest("", "444.444.444-00", "500.00", "10", true);

        ValidationException thrown = assertThrows(ValidationException.class, () -> clienteService.createCliente(request));
        assertEquals("Nome do cliente não pode ser vazio.", thrown.getMessage());
    }

    @Test
    public void deveNegarCadastroSemDocumento() {
        RegisterUpdateClienteRequest request = new RegisterUpdateClienteRequest("Guilherme", "", "500.00", "10", false);

        ValidationException thrown = assertThrows(ValidationException.class, () -> clienteService.createCliente(request));
        assertEquals("CPF/CNPJ deve ser válido!", thrown.getMessage());
    }

    @Test
    public void deveNegarCadastroComLimiteZero() {
        RegisterUpdateClienteRequest request = new RegisterUpdateClienteRequest("Guilherme", "444.444.444-00", "", "10", false);

        ValidationException thrown = assertThrows(ValidationException.class, () -> clienteService.createCliente(request));
        assertEquals("O limite de crédito deve ser um numero válido!", thrown.getMessage());
    }

    @Test
    public void deveNegarCadastroComDiaDeFechamentoInvalido() {
        RegisterUpdateClienteRequest request = new RegisterUpdateClienteRequest("Guilherme", "444.444.444-00", "500.00", "32", false);

        ValidationException thrown = assertThrows(ValidationException.class, () -> clienteService.createCliente(request));
        assertEquals("Dia de fechamento da fatura deve ser entre 1 e 31.", thrown.getMessage());
    }

    @Test
    public void deveRetornarClienteExistente() throws SQLException {
        String cpfCnpj = "444.444.444-00";
        Cliente clienteEsperado = new Cliente(1L, "Guilherme", "444.444.444-00", new BigDecimal(500.00), 10, true);
        Mockito.when(clienteDAO.getClienteByCpfCnpj(cpfCnpj)).thenReturn(Optional.of(clienteEsperado));
        Cliente clienteRetornado = clienteService.getClienteByDoc(cpfCnpj);
        assertNotNull(clienteRetornado);
        assertEquals(clienteEsperado, clienteRetornado);
    }

    @Test
    public void deveRetornarExcecaoParaClienteInexistente() throws SQLException {
        String cpfCnpj = "444.444.444-00";
        Mockito.when(clienteDAO.getClienteByCpfCnpj(cpfCnpj)).thenReturn(Optional.empty());
        assertThrows(ClienteNotFoundException.class, () -> clienteService.getClienteByDoc(cpfCnpj));
    }

    @Test
    public void deveAtualizarClienteComDadosValidos() throws SQLException {
        RegisterUpdateClienteRequest request = new RegisterUpdateClienteRequest("Guilherme", "444.444.444-00", "500.00", "10", true);
        Mockito.doNothing().when(clienteDAO).updateCliente(Mockito.any(Cliente.class));
        clienteService.updateCliente(request);
        verify(clienteDAO, times(1)).updateCliente(Mockito.any(Cliente.class));
    }

    @Test
    public void deveNegarUpdateSemNome() {
        RegisterUpdateClienteRequest request = new RegisterUpdateClienteRequest("", "444.444.444-00", "500.00", "10", false);

        ValidationException thrown = assertThrows(ValidationException.class, () -> clienteService.updateCliente(request));
        assertEquals("Nome do cliente não pode ser vazio.", thrown.getMessage());
    }

    @Test
    public void deveNegarUpdateSemDocumento() {
        RegisterUpdateClienteRequest request = new RegisterUpdateClienteRequest("Guilherme", "", "500.00", "10", false);

        ValidationException thrown = assertThrows(ValidationException.class, () -> clienteService.updateCliente(request));
        assertEquals("CPF/CNPJ deve ser válido!", thrown.getMessage());
    }

    @Test
    public void deveNegarUpdateComLimiteZero() {
        RegisterUpdateClienteRequest request = new RegisterUpdateClienteRequest("Guilherme", "444.444.444-00", "0", "10", true);

        ValidationException thrown = assertThrows(ValidationException.class, () -> clienteService.updateCliente(request));
        assertEquals("O limite de crédito deve ser um numero válido!", thrown.getMessage());
    }

    @Test
    public void deveNegarUpdateComDiaDeFechamentoInvalido() {
        RegisterUpdateClienteRequest request = new RegisterUpdateClienteRequest("Guilherme", "444.444.444-00", "500.00", "32", true);

        ValidationException thrown = assertThrows(ValidationException.class, () -> clienteService.updateCliente(request));
        assertEquals("Dia de fechamento da fatura deve ser entre 1 e 31.", thrown.getMessage());
    }

    @Test
    public void deveRetornarListaDeClientesPreenchida() throws SQLException {
        Cliente cliente1 = new Cliente(1L, "Guilherme", "444.444.444-00", new BigDecimal(500.00), 10, true);
        Cliente cliente2 = new Cliente(2L, "João", "555.555.555-00", new BigDecimal(1000.00), 15, true);
        List<Cliente> clienteList = Arrays.asList(cliente1, cliente2);
        Mockito.when(clienteDAO.getAllClientes()).thenReturn(clienteList);

        List<Cliente> clientes = clienteService.getAllClientes();
        assertNotNull(clientes);
        assertIterableEquals(clienteList, clientes);
        Mockito.verify(clienteDAO, times(1)).getAllClientes();
    }

    @Test
    public void deveRetornarListaDeClientesVazia() throws SQLException {
        Mockito.when(clienteDAO.getAllClientes()).thenReturn(new ArrayList<>());
        List<Cliente> clientes = clienteService.getAllClientes();
        assertNotNull(clientes);
        assertEquals(0, clientes.size());
        Mockito.verify(clienteDAO, times(1)).getAllClientes();
    }

    @Test
    public void deveDeletarcliente() throws SQLException {
        String cpfCnpj = "444.444.444-00";
        Mockito.doNothing().when(clienteDAO).deleteCliente(cpfCnpj);
        assertDoesNotThrow(() -> clienteService.deleteCliente(cpfCnpj));
        verify(clienteDAO, times(1)).deleteCliente(cpfCnpj);
    }

    @Test
    public void deveInativarCliente() throws SQLException {
        String cpfCnpj = "444.444.444-00";
        Mockito.doNothing().when(clienteDAO).inativaCliente(cpfCnpj);
        assertDoesNotThrow(() -> clienteService.inativarCliente(cpfCnpj));
        verify(clienteDAO, times(1)).inativaCliente(cpfCnpj);
    }

}

