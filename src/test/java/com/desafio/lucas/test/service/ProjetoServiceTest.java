package com.desafio.lucas.test.service;

import com.desafio.lucas.dto.MembroDTO;
import com.desafio.lucas.dto.RelatorioPortfolioDTO;
import com.desafio.lucas.exception.RegraNegocioException;
import com.desafio.lucas.model.Projeto;
import com.desafio.lucas.model.enums.StatusProjeto;
import com.desafio.lucas.repository.ProjetoRepository;
import com.desafio.lucas.service.MockMembroService;
import com.desafio.lucas.service.ProjetoService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjetoServiceTest {

    @InjectMocks
    private ProjetoService service;

    @Mock
    private ProjetoRepository repository;

    @Mock
    private MockMembroService membroService;

    @Test
    void deveLancarErroAoExcluirProjetoEmAndamento() {
        Projeto p = new Projeto();
        p.setStatus(StatusProjeto.EM_ANDAMENTO);

        when(repository.findById(1L)).thenReturn(Optional.of(p));

        assertThrows(RegraNegocioException.class, () -> service.excluir(1L));
    }

    @Test
    void deveExcluirProjetoComSucesso() {
        Projeto p = new Projeto();
        p.setStatus(StatusProjeto.EM_ANALISE);

        when(repository.findById(1L)).thenReturn(Optional.of(p));

        service.excluir(1L);

        verify(repository).delete(p);
    }

    @Test
    void deveAlterarStatusComSucesso() {
        Projeto p = new Projeto();
        p.setStatus(StatusProjeto.EM_ANALISE);

        when(repository.findById(1L)).thenReturn(Optional.of(p));
        when(repository.save(any(Projeto.class))).thenReturn(p);
        Projeto result = service.alterarStatus(1L, StatusProjeto.ANALISE_REALIZADA);
        assertEquals(StatusProjeto.ANALISE_REALIZADA, result.getStatus());
    }

    @Test
    void deveLancarErroTransicaoInvalida() {
        Projeto p = new Projeto();
        p.setStatus(StatusProjeto.ENCERRADO);

        when(repository.findById(1L)).thenReturn(Optional.of(p));

        assertThrows(RegraNegocioException.class,
                () -> service.alterarStatus(1L, StatusProjeto.INICIADO));
    }

    @Test
    void deveAssociarMembroComSucesso() {

        Projeto p = new Projeto();
        p.setStatus(StatusProjeto.EM_ANALISE);
        p.setMembrosIds(new HashSet<>());

        MembroDTO membro = new MembroDTO(1L, "Lucas", "funcionário");

        when(repository.findById(1L)).thenReturn(Optional.of(p));
        when(membroService.buscarMembro(1L)).thenReturn(membro);
        when(repository.countProjetosAtivosPorMembro(anyLong(), anyList()))
                .thenReturn(0L);
        when(repository.save(any(Projeto.class))).thenReturn(p);

        Projeto result = service.associarMembro(1L, 1L);

        assertTrue(result.getMembrosIds().contains(1L));
    }

    @Test
    void deveLancarErroQuandoMembroNaoForFuncionario() {
        Projeto p = new Projeto();
        p.setMembrosIds(new HashSet<>());

        MembroDTO membro = new MembroDTO(1L, "Lucas", "gerente");

        when(repository.findById(1L)).thenReturn(Optional.of(p));
        when(membroService.buscarMembro(1L)).thenReturn(membro);

        assertThrows(RegraNegocioException.class,
                () -> service.associarMembro(1L, 1L));
    }

    @Test
    void deveLancarErroQuandoExcederLimiteDeMembros() {
        Projeto p = new Projeto();
        p.setMembrosIds(new HashSet<>());

        for (int i = 0; i < 10; i++) {
            p.getMembrosIds().add((long) i);
        }

        MembroDTO membro = new MembroDTO(11L, "Lucas", "funcionário");

        when(repository.findById(1L)).thenReturn(Optional.of(p));
        when(membroService.buscarMembro(11L)).thenReturn(membro);

        assertThrows(RegraNegocioException.class,
                () -> service.associarMembro(1L, 11L));
    }

    @Test
    void deveLancarErroQuandoMembroExcederProjetosAtivos() {
        Projeto p = new Projeto();
        p.setMembrosIds(new HashSet<>());

        MembroDTO membro = new MembroDTO(1L, "Lucas","funcionário");

        when(repository.findById(1L)).thenReturn(Optional.of(p));
        when(membroService.buscarMembro(1L)).thenReturn(membro);
        when(repository.countProjetosAtivosPorMembro(anyLong(), anyList())).thenReturn(3L);

        assertThrows(RegraNegocioException.class,
                () -> service.associarMembro(1L, 1L));
    }

    @Test
    void deveGerarRelatorioComSucesso() {
        Projeto p = new Projeto();
        p.setStatus(StatusProjeto.ENCERRADO);
        p.setDataInicio(LocalDate.now().minusDays(10));
        p.setDataRealTermino(LocalDate.now());
        p.setOrcamentoTotal(BigDecimal.TEN);
        p.setMembrosIds(new HashSet<>(Arrays.asList(1L, 2L)));
        when(repository.findAll()).thenReturn(List.of(p));

        RelatorioPortfolioDTO relatorio = service.gerarRelatorio();

        assertNotNull(relatorio);
    }
}