package com.estoque.sistemacontroleestoque.controller;

import com.estoque.sistemacontroleestoque.model.Produto;
import com.estoque.sistemacontroleestoque.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<Produto> criar(@Valid @RequestBody Produto produto) {
        Produto novoProduto = produtoService.criar(produto);
        return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> obterPorId(@PathVariable Long id) {
        Produto produto = produtoService.obterPorId(id);
        return ResponseEntity.ok(produto);
    }

    @GetMapping
    public ResponseEntity<Page<Produto>> listarTodos(Pageable pageable) {
        Page<Produto> produtos = produtoService.listarTodos(pageable);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<Produto> obterPorSku(@PathVariable String sku) {
        Produto produto = produtoService.obterPorSku(sku);
        return ResponseEntity.ok(produto);
    }

    @GetMapping("/buscar/nome")
    public ResponseEntity<List<Produto>> buscarPorNome(@RequestParam String nome) {
        List<Produto> produtos = produtoService.buscarPorNome(nome);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/buscar/categoria")
    public ResponseEntity<List<Produto>> buscarPorCategoria(@RequestParam String categoria) {
        List<Produto> produtos = produtoService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/buscar/termo")
    public ResponseEntity<List<Produto>> buscar(@RequestParam String termo) {
        List<Produto> produtos = produtoService.buscar(termo);
        return ResponseEntity.ok(produtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @Valid @RequestBody Produto produto) {
        Produto produtoAtualizado = produtoService.atualizar(id, produto);
        return ResponseEntity.ok(produtoAtualizado);
    }

    @PatchMapping("/{id}/estoque")
    public ResponseEntity<Void> atualizarEstoque(@PathVariable Long id, @RequestParam Integer quantidade) {
        produtoService.atualizarEstoque(id, quantidade);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/alertas/estoque-minimo")
    public ResponseEntity<List<Produto>> obterAvisoEstoqueMinimo() {
        List<Produto> produtos = produtoService.obterProdutosAbaixoEstoqueMinimo();
        return ResponseEntity.ok(produtos);
    }
}
