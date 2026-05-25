package com.estoque.sistemacontroleestoque.exception;

public class ProdutoJaExisteException extends RuntimeException {
    public ProdutoJaExisteException(String mensagem) {
        super(mensagem);
    }
}
