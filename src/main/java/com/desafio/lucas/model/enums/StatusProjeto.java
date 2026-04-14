package com.desafio.lucas.model.enums;

public enum StatusProjeto {
    EM_ANALISE, ANALISE_REALIZADA, ANALISE_APROVADA, INICIADO,
    PLANEJADO, EM_ANDAMENTO, ENCERRADO, CANCELADO;

    public boolean podeTransitarPara(StatusProjeto novoStatus) {
        if (novoStatus == CANCELADO) return true;
        return switch (this) {
            case EM_ANALISE -> novoStatus == ANALISE_REALIZADA;
            case ANALISE_REALIZADA -> novoStatus == ANALISE_APROVADA;
            case ANALISE_APROVADA -> novoStatus == INICIADO;
            case INICIADO -> novoStatus == PLANEJADO;
            case PLANEJADO -> novoStatus == EM_ANDAMENTO;
            case EM_ANDAMENTO -> novoStatus == ENCERRADO;
            case ENCERRADO, CANCELADO -> false;
        };
    }
}
