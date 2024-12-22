import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.ClienteDAO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.ClienteRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.RegisterClienteRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Cliente;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.ClienteService;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.impl.ClienteServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class ClienteServiceTest {
    private ClienteDAO clienteDAO;
    private ClienteService clienteService;

    @BeforeEach
    private void setUp() {
        clienteDAO = Mockito.mock(ClienteDAO.class);
        clienteService = new ClienteServiceImpl(clienteDAO);
    }

    @Test
    public void deveRegistrarUmCliente() throws SQLException {
        RegisterClienteRequest request = new RegisterClienteRequest("Guilherme", "444.444.444-00", new BigDecimal(500.00), 10);

        Mockito.doNothing().when(clienteDAO).createCliente(Mockito.any(Cliente.class));

        Assertions.assertDoesNotThrow(() -> clienteService.createCliente(request));

        verify(clienteDAO, times(1)).createCliente(Mockito.any(Cliente.class));
    }

    @Test
    public void deveNegarCadastroSemNome() {
        RegisterClienteRequest request = new RegisterClienteRequest("", "444.444.444-00", new BigDecimal(500.00), 10);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> clienteService.createCliente(request));
        assertEquals("Nome do cliente não pode ser vazio.", thrown.getMessage());
    }

    @Test
    public void deveNegarCadastroSemDocumento() {
        RegisterClienteRequest request = new RegisterClienteRequest("Guilherme", "", new BigDecimal(500.00), 10);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> clienteService.createCliente(request));
        assertEquals("CPF/CNPJ não pode ser vazio.", thrown.getMessage());
    }

    @Test
    public void deveNegarCadastroComLimiteZero() {
        RegisterClienteRequest request = new RegisterClienteRequest("Guilherme", "444.444.444-00", BigDecimal.ZERO, 10);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> clienteService.createCliente(request));
        assertEquals("O limite de crédito deve ser maior que zero.", thrown.getMessage());
    }

    @Test
    public void deveNegarCadastroComDiaDeFechamentoInvalido() {
        RegisterClienteRequest request = new RegisterClienteRequest("Guilherme", "444.444.444-00", new BigDecimal(500.00), 32);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> clienteService.createCliente(request));
        assertEquals("Dia de fechamento da fatura deve ser entre 1 e 31.", thrown.getMessage());
    }

    @Test
    public void deveRetornarClienteExistente() throws SQLException {
        ClienteRequest request = new ClienteRequest("444.444.444-00");
        Cliente clienteEsperado = new Cliente(1L, "Guilherme", "444.444.444-00", new BigDecimal(500.00), 10);
        Mockito.when(clienteDAO.getClienteByCpfCnpj(request.cpfCnpj())).thenReturn(Optional.of(clienteEsperado));
        Cliente clienteRetornado = clienteService.getClienteByDoc(request );
        assertNotNull(clienteRetornado);
        assertEquals(clienteEsperado, clienteRetornado);
    }

    @Test
    public void deveRetornarExcecaoParaClienteInexistente() throws SQLException {
        ClienteRequest request = new ClienteRequest("444.444.444-00");
        Mockito.when(clienteDAO.getClienteByCpfCnpj(request.cpfCnpj())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> clienteService.getClienteByDoc(request));
    }
}

