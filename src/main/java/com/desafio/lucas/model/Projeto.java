package com.desafio.lucas.model;

import com.desafio.lucas.model.enums.Risco;
import com.desafio.lucas.model.enums.StatusProjeto;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "projetos")
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private LocalDate dataInicio;
    private LocalDate previsaoTermino;
    private LocalDate dataRealTermino;

    @Column(precision = 15, scale = 2)
    private BigDecimal orcamentoTotal;

    @Column(length = 500)
    private String descricao;

    private Long idGerente;

    @Enumerated(EnumType.STRING)
    private StatusProjeto status = StatusProjeto.EM_ANALISE;

    @ElementCollection
    @CollectionTable(name = "projeto_membros", joinColumns = @JoinColumn(name = "projeto_id"))
    @Column(name = "membro_id")
    private Set<Long> membrosIds = new HashSet<>();

    @Transient
    public Risco getRisco() {
        if (orcamentoTotal == null || dataInicio == null || previsaoTermino == null) {
            return Risco.BAIXO_RISCO;
        }

        long mesesRestantes = ChronoUnit.MONTHS.between(dataInicio, previsaoTermino);
        boolean orcamentoBaixo = orcamentoTotal.compareTo(new BigDecimal("100000")) <= 0;
        boolean prazoCurto = mesesRestantes <= 3;
        boolean orcamentoAlto = orcamentoTotal.compareTo(new BigDecimal("500000")) > 0;
        boolean prazoLongo = mesesRestantes > 6;

        if (orcamentoAlto || prazoLongo) return Risco.ALTO_RISCO;
        if (orcamentoBaixo && prazoCurto) return Risco.BAIXO_RISCO;
        return Risco.MEDIO_RISCO;
    }
}