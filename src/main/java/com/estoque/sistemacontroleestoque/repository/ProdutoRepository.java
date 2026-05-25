package com.estoque.sistemacontroleestoque.repository;

import com.estoque.sistemacontroleestoque.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findBySku(String sku);

    List<Produto> findByNomeContainingIgnoreCase(String nome);

    List<Produto> findByCategoria(String categoria);

    Page<Produto> findAll(Pageable pageable);

    @Query("SELECT p FROM Produto p WHERE p.quantidade < p.quantidadeMinima")
    List<Produto> findProdutosAbaixoEstoqueMinimo();

    @Query("SELECT p FROM Produto p WHERE p.categoria = :categoria ORDER BY p.preco ASC")
    List<Produto> findByCategoriaOrderByPreco(@Param("categoria") String categoria);

    @Query("SELECT p FROM Produto p WHERE p.nome LIKE %:termo% OR p.descricao LIKE %:termo%")
    List<Produto> buscarPorTermo(@Param("termo") String termo);
}
