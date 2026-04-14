package com.desafio.lucas.exception;

public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}