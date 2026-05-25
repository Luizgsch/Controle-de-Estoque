package com.estoque.sistemacontroleestoque.service;

import com.estoque.sistemacontroleestoque.model.Produto;
import com.estoque.sistemacontroleestoque.repository.ProdutoRepository;
import com.estoque.sistemacontroleestoque.exception.ProdutoNaoEncontradoException;
import com.estoque.sistemacontroleestoque.exception.ProdutoJaExisteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto criar(Produto produto) {
        if (produtoRepository.findBySku(produto.getSku()).isPresent()) {
            throw new ProdutoJaExisteException("Produto com SKU " + produto.getSku() + " já existe");
        }
        produto.setDataCriacao(LocalDateTime.now());
        produto.setDataAtualizacao(LocalDateTime.now());
        return produtoRepository.save(produto);
    }

    public Produto obterPorId(Long id) {
        return produtoRepository.findById(id)
            .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado: " + id));
    }

    public Page<Produto> listarTodos(Pageable pageable) {
        return produtoRepository.findAll(pageable);
    }

    public List<Produto> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Produto> buscarPorCategoria(String categoria) {
        return produtoRepository.findByCategoria(categoria);
    }

    public Produto obterPorSku(String sku) {
        return produtoRepository.findBySku(sku)
            .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto com SKU " + sku + " não encontrado"));
    }

    public Produto atualizar(Long id, Produto produtoAtualizado) {
        Produto produto = obterPorId(id);
        
        if (!produto.getSku().equals(produtoAtualizado.getSku()) && 
            produtoRepository.findBySku(produtoAtualizado.getSku()).isPresent()) {
            throw new ProdutoJaExisteException("SKU já existe");
        }

        produto.setNome(produtoAtualizado.getNome());
        produto.setDescricao(produtoAtualizado.getDescricao());
        produto.setPreco(produtoAtualizado.getPreco());
        produto.setQuantidade(produtoAtualizado.getQuantidade());
        produto.setQuantidadeMinima(produtoAtualizado.getQuantidadeMinima());
        produto.setSku(produtoAtualizado.getSku());
        produto.setCategoria(produtoAtualizado.getCategoria());
        produto.setDataAtualizacao(LocalDateTime.now());

        return produtoRepository.save(produto);
    }

    public void deletar(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ProdutoNaoEncontradoException("Produto não encontrado: " + id);
        }
        produtoRepository.deleteById(id);
    }

    public List<Produto> obterProdutosAbaixoEstoqueMinimo() {
        return produtoRepository.findProdutosAbaixoEstoqueMinimo();
    }

    public void atualizarEstoque(Long id, Integer quantidade) {
        Produto produto = obterPorId(id);
        produto.setQuantidade(quantidade);
        produto.setDataAtualizacao(LocalDateTime.now());
        produtoRepository.save(produto);
    }

    public List<Produto> buscar(String termo) {
        return produtoRepository.buscarPorTermo(termo);
    }
}
