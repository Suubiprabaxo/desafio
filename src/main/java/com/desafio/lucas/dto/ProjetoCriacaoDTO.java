package com.desafio.lucas.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ProjetoCriacaoDTO(

        @NotBlank
        String nome,

        @NotNull
        LocalDate dataInicio,

        @NotNull
        LocalDate previsaoTermino,

        LocalDate dataRealTermino,

        @NotNull
        @DecimalMin("0.0")
        BigDecimal orcamentoTotal,

        @Size(max = 500)
        String descricao,

        Long idGerente
) {}