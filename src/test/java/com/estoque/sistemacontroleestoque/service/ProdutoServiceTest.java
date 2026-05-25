package com.estoque.sistemacontroleestoque.service;

import com.estoque.sistemacontroleestoque.model.Produto;
import com.estoque.sistemacontroleestoque.repository.ProdutoRepository;
import com.estoque.sistemacontroleestoque.exception.ProdutoNaoEncontradoException;
import com.estoque.sistemacontroleestoque.exception.ProdutoJaExisteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;

    @BeforeEach
    void setUp() {
        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        produto.setDescricao("Descrição teste");
        produto.setPreco(new BigDecimal("99.99"));
        produto.setQuantidade(10);
        produto.setQuantidadeMinima(5);
        produto.setSku("SKU001");
        produto.setCategoria("Categoria Teste");
    }

    @Test
    void deveCriarProdutoComSucesso() {
        when(produtoRepository.findBySku("SKU001")).thenReturn(Optional.empty());
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        Produto resultado = produtoService.criar(produto);

        assertNotNull(resultado);
        assertEquals("Produto Teste", resultado.getNome());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void deveThrowExceptionAoCriarProdutoDuplicado() {
        when(produtoRepository.findBySku("SKU001")).thenReturn(Optional.of(produto));

        assertThrows(ProdutoJaExisteException.class, () -> {
            produtoService.criar(produto);
        });
    }

    @Test
    void deveObterProdutoPorIdComSucesso() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Produto resultado = produtoService.obterPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(produtoRepository, times(1)).findById(1L);
    }

    @Test
    void deveThrowExceptionAoObterProdutoInexistente() {
        when(produtoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProdutoNaoEncontradoException.class, () -> {
            produtoService.obterPorId(999L);
        });
    }

    @Test
    void deveAtualizarProdutoComSucesso() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.findBySku("SKU002")).thenReturn(Optional.empty());
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setNome("Produto Atualizado");
        produtoAtualizado.setDescricao("Descrição atualizada");
        produtoAtualizado.setPreco(new BigDecimal("199.99"));
        produtoAtualizado.setQuantidade(20);
        produtoAtualizado.setQuantidadeMinima(10);
        produtoAtualizado.setSku("SKU002");
        produtoAtualizado.setCategoria("Nova Categoria");

        Produto resultado = produtoService.atualizar(1L, produtoAtualizado);

        assertNotNull(resultado);
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void deveDeletarProdutoComSucesso() {
        when(produtoRepository.existsById(1L)).thenReturn(true);

        produtoService.deletar(1L);

        verify(produtoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveThrowExceptionAoDeletarProdutoInexistente() {
        when(produtoRepository.existsById(999L)).thenReturn(false);

        assertThrows(ProdutoNaoEncontradoException.class, () -> {
            produtoService.deletar(999L);
        });
    }
}
