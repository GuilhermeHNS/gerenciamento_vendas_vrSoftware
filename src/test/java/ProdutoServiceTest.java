import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dao.ProdutoDAO;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.RegisterProdutoRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.dtos.request.UpdateProdutoRequest;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.exceptions.ProdutoNotFoundException;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.exceptions.ValidationException;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.model.Produto;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.ProdutoService;
import com.GuilhermeHNS.gerenciamento_vendas_vrSoftware.service.impl.ProdutoServiceImpl;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
public class ProdutoServiceTest {
    private ProdutoDAO produtoDAO;
    private ProdutoService produtoService;

    @BeforeEach
    public void setUp() {
        produtoDAO = Mockito.mock(ProdutoDAO.class);
        produtoService = new ProdutoServiceImpl(produtoDAO);
    }

    @Test
    public void deveRegistrarUmProdutoCorretamente() throws SQLException {
        RegisterProdutoRequest request = new RegisterProdutoRequest("Bola de futebol", "80", true);

        Mockito.doNothing().when(produtoDAO).createProduto(Mockito.any(Produto.class));

        Assertions.assertDoesNotThrow(() -> produtoService.createProduto(request));

        verify(produtoDAO, times(1)).createProduto(Mockito.any(Produto.class));
    }

    @Test
    public void deveNegarRegistroSemDescricao() throws SQLException {
        RegisterProdutoRequest request = new RegisterProdutoRequest("", "80", true);

        ValidationException thrown = assertThrows(ValidationException.class, () -> produtoService.createProduto(request));
        assertEquals("A descrição do produto é obrigatória!", thrown.getMessage());
    }

    @Test
    public void deveNegarRegistroSemPreco() throws SQLException {
        RegisterProdutoRequest request = new RegisterProdutoRequest("Bola de futebol", "", true);
        ValidationException thrown = assertThrows(ValidationException.class, () -> produtoService.createProduto(request));
        assertEquals("O preço do produto não pode ser menor que 0!", thrown.getMessage());
    }

    @Test
    public void deveNegarRegistroComPrecoInvalido() throws SQLException {
        RegisterProdutoRequest request = new RegisterProdutoRequest("Bola de futebol", "abcd", true);
        ValidationException thrown = assertThrows(ValidationException.class, () -> produtoService.createProduto(request));
        assertEquals("O preço do produto não pode ser menor que 0!", thrown.getMessage());
    }

    @Test
    public void deveRetornarProdutoComIdExistente() throws SQLException {
        String codigoProduto = "1";
        Produto produtoEsperado = new Produto(1L, "Bola de futebol", new BigDecimal(80), true);
        Mockito.when(produtoDAO.findProdutoById(Long.parseLong(codigoProduto))).thenReturn(Optional.of(produtoEsperado));
        Produto produtoRetornado = produtoService.getProdutoById(codigoProduto);
        assertNotNull(produtoRetornado);
        assertEquals(produtoEsperado, produtoRetornado);
    }
    @Test
    public void deveRetornarExcecaoParaProdutoComCodigoNaoEncontrado() throws SQLException {
        String codigoProduto = "1";
        Mockito.when(produtoDAO.findProdutoById(Long.parseLong(codigoProduto))).thenReturn(Optional.empty());
        assertThrows(ProdutoNotFoundException.class, () -> produtoService.getProdutoById(codigoProduto));
    }
    @Test
    public void deveRetornarProdutosComDescricaoExistenteEParecidas() throws SQLException {
        String desc = "Bola";
        Produto produto01 = new Produto(1L, "Bola de futebol", new BigDecimal(100), true);
        Produto produto02 = new Produto(1L, "Bola de volei", new BigDecimal(80), true);
        Produto produto03 = new Produto(1L, "Bola de basquete", new BigDecimal(95), true);
        List<Produto> produtoListEsperado = Arrays.asList(produto01, produto02, produto03);
        Mockito.when(produtoDAO.findProdutoByDesc(desc)).thenReturn(produtoListEsperado);

        List<Produto> produtoList = produtoService.getProdutoByDescricao(desc);
        assertNotNull(produtoList);
        assertIterableEquals(produtoList, produtoListEsperado);
        Mockito.verify(produtoDAO, times(1)).findProdutoByDesc(desc);
    }
    @Test
    public void deveRetornarListaDeProdutosVaziaPorDescricaoNaoEncontrada() throws SQLException {
        String desc = "Panela";
        Mockito.when(produtoDAO.findProdutoByDesc(desc)).thenReturn(new ArrayList<>());
        List<Produto> produtoList = produtoService.getProdutoByDescricao(desc);
        assertNotNull(produtoList);
        assertEquals(0, produtoList.size());
        Mockito.verify(produtoDAO, times(1)).findProdutoByDesc(desc);
    }

    @Test
    public void deveRetornarTodosOsProdutos() throws SQLException {
        Produto produto01 = new Produto(1L, "Bola de futebol", new BigDecimal(100), true);
        Produto produto02 = new Produto(1L, "Bola de volei", new BigDecimal(80), true);
        Produto produto03 = new Produto(1L, "Bola de basquete", new BigDecimal(95), true);
        List<Produto> produtoListEsperado = Arrays.asList(produto01, produto02, produto03);
        Mockito.when(produtoDAO.findAll()).thenReturn(produtoListEsperado);

        List<Produto> produtoList = produtoService.getAllProdutos();
        assertNotNull(produtoList);
        assertIterableEquals(produtoList, produtoListEsperado);
        Mockito.verify(produtoDAO, times(1)).findAll();
    }

    @Test
    public void deveRetornarListaVazia() throws SQLException {
        Mockito.when(produtoDAO.findAll()).thenReturn(new ArrayList<>());
        List<Produto> produtoList = produtoService.getAllProdutos();
        assertNotNull(produtoList);
        assertEquals(0, produtoList.size());
        Mockito.verify(produtoDAO, times(1)).findAll();
    }

    @Test
    public void deveAtualizarProdutoComDadosValidos() throws SQLException {
        UpdateProdutoRequest request = new UpdateProdutoRequest(1L, "Bola de Volei", "80.0", false);
        Mockito.doNothing().when(produtoDAO).updateProduto(Mockito.any(Produto.class));
        Assertions.assertDoesNotThrow(() -> produtoService.updateProduto(request));
        verify(produtoDAO, times(1)).updateProduto(Mockito.any(Produto.class));
    }

    @Test
    public void deveLancarExcecaoQuandoTentarAtualizarProdutoComDescricaoVazia() throws SQLException {
        UpdateProdutoRequest request = new UpdateProdutoRequest(1L, "", "80.0", false);
        ValidationException thrown = assertThrows(ValidationException.class, () -> produtoService.updateProduto(request));
        assertEquals("A descrição do produto é obrigatória!", thrown.getMessage());

    }

    @Test
    public void deveLancarExcecaoQuandoTentarAtualizarProdutoComValorInvalido() throws SQLException {
        UpdateProdutoRequest request = new UpdateProdutoRequest(1L, "Bola de futebol", "asd", true);
        ValidationException thrown = assertThrows(ValidationException.class, () -> produtoService.updateProduto(request));
        assertEquals("O preço do produto não pode ser menor que 0!", thrown.getMessage());
    }

    @Test
    public void deveDeletarProdutoCorretamente() throws SQLException {
        Long idProduto = 1L;
        Mockito.doNothing().when(produtoDAO).deleteProduto(idProduto);
        assertDoesNotThrow(() -> produtoService.deleteProduto(idProduto));
        verify(produtoDAO, times(1)).deleteProduto(idProduto);
    }

    @Test
    public void deveInativarProduto() throws SQLException {
        Long idProduto = 1L;
        Mockito.doNothing().when(produtoDAO).inativaProduto(idProduto);
        assertDoesNotThrow(() -> produtoService.inativarProduto(idProduto));
        verify(produtoDAO, times(1)).inativaProduto(idProduto);
    }
}
