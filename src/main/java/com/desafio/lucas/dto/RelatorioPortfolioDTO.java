package com.desafio.lucas.dto;

import java.math.BigDecimal;
import java.util.Map;

public record RelatorioPortfolioDTO(
        Map<String, Long> quantidadePorStatus,
        Map<String, BigDecimal> orcamentoPorStatus,
        Double mediaDuracaoDiasProjetosEncerrados,
        Long totalMembrosUnicos
) {}