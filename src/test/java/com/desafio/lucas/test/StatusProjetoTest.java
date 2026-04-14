package com.desafio.lucas.test;

import com.desafio.lucas.model.enums.StatusProjeto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class StatusProjetoTest {

    @Test
    void devePermitirTransicaoLogicaCorreta() {
        assertTrue(StatusProjeto.EM_ANALISE.podeTransitarPara(StatusProjeto.ANALISE_REALIZADA));
        assertTrue(StatusProjeto.INICIADO.podeTransitarPara(StatusProjeto.PLANEJADO));
    }

    @Test
    void naoDevePermitirPularEtapas() {
        assertFalse(StatusProjeto.EM_ANALISE.podeTransitarPara(StatusProjeto.INICIADO));
        assertFalse(StatusProjeto.PLANEJADO.podeTransitarPara(StatusProjeto.ENCERRADO));
    }

    @Test
    void devePermitirCancelamentoDeQualquerStatusAtivo() {
        assertTrue(StatusProjeto.EM_ANALISE.podeTransitarPara(StatusProjeto.CANCELADO));
        assertTrue(StatusProjeto.EM_ANDAMENTO.podeTransitarPara(StatusProjeto.CANCELADO));
    }
}