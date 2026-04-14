package com.desafio.lucas.dto;

import com.desafio.lucas.model.enums.Risco;
import com.desafio.lucas.model.enums.StatusProjeto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record ProjetoDTO(
        Long id,
        String nome,
        LocalDate dataInicio,
        LocalDate previsaoTermino,
        LocalDate dataRealTermino,
        BigDecimal orcamentoTotal,
        String descricao,
        Long idGerente,
        StatusProjeto status,
        Risco risco,
        Set<Long> membrosIds
) {}