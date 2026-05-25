package com.estoque.sistemacontroleestoque.controller;

import com.estoque.sistemacontroleestoque.auth.JwtService;
import com.estoque.sistemacontroleestoque.model.Produto;
import com.estoque.sistemacontroleestoque.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdutoController.class)
@WithMockUser
public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService produtoService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void deveCriarProdutoComStatus201() throws Exception {
        when(produtoService.criar(any(Produto.class))).thenReturn(produto);

        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Produto Teste"));

        verify(produtoService, times(1)).criar(any(Produto.class));
    }

    @Test
    void deveObterProdutoPorIdComStatus200() throws Exception {
        when(produtoService.obterPorId(1L)).thenReturn(produto);

        mockMvc.perform(get("/api/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Produto Teste"));

        verify(produtoService, times(1)).obterPorId(1L);
    }

    @Test
    void deveAtualizarProdutoComStatus200() throws Exception {
        when(produtoService.atualizar(eq(1L), any(Produto.class))).thenReturn(produto);

        mockMvc.perform(put("/api/produtos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(produtoService, times(1)).atualizar(eq(1L), any(Produto.class));
    }

    @Test
    void deveDeletarProdutoComStatus204() throws Exception {
        doNothing().when(produtoService).deletar(1L);

        mockMvc.perform(delete("/api/produtos/1"))
                .andExpect(status().isNoContent());

        verify(produtoService, times(1)).deletar(1L);
    }
}
