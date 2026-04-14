package com.desafio.lucas.service;

import com.desafio.lucas.dto.MembroDTO;
import com.desafio.lucas.dto.ProjetoCriacaoDTO;
import com.desafio.lucas.dto.RelatorioPortfolioDTO;
import com.desafio.lucas.exception.RecursoNaoEncontradoException;
import com.desafio.lucas.exception.RegraNegocioException;
import com.desafio.lucas.mapper.ProjetoMapper;
import com.desafio.lucas.model.Projeto;
import com.desafio.lucas.model.enums.StatusProjeto;
import com.desafio.lucas.repository.ProjetoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final MockMembroService membroService;
    private final ProjetoMapper projetoMapper;

    public Page<Projeto> listar(StatusProjeto status, Pageable pageable) {
        if (status != null) {
            return projetoRepository.findByStatus(status, pageable);
        }
        return projetoRepository.findAll(pageable);
    }

    public Projeto buscarPorId(Long id) {
        return projetoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Projeto não encontrado"));
    }

    @Transactional
    public Projeto salvar(ProjetoCriacaoDTO dto) {
        Projeto projeto = projetoMapper.toEntity(dto);
        projeto.setStatus(StatusProjeto.EM_ANALISE);
        return projetoRepository.save(projeto);
    }

    @Transactional
    public void excluir(Long id) {
        Projeto projeto = buscarPorId(id);
        if (List.of(StatusProjeto.INICIADO, StatusProjeto.EM_ANDAMENTO, StatusProjeto.ENCERRADO).contains(projeto.getStatus())) {
            throw new RegraNegocioException("Projetos com status Iniciado, Em andamento ou Encerrado não podem ser excluídos.");
        }
        projetoRepository.delete(projeto);
    }

    @Transactional
    public Projeto alterarStatus(Long id, StatusProjeto novoStatus) {
        Projeto projeto = buscarPorId(id);
        if (!projeto.getStatus().podeTransitarPara(novoStatus)) {
            throw new RegraNegocioException("Transição de status inválida de " + projeto.getStatus() + " para " + novoStatus);
        }
        projeto.setStatus(novoStatus);
        return projetoRepository.save(projeto);
    }

    @Transactional
    public Projeto associarMembro(Long projetoId, Long membroId) {
        Projeto projeto = buscarPorId(projetoId);
        MembroDTO membro = membroService.buscarMembro(membroId);

        if (!"funcionário".equalsIgnoreCase(membro.atribuicao())) {
            throw new RegraNegocioException("Apenas membros com atribuição 'funcionário' podem ser associados.");
        }

        if (projeto.getMembrosIds().size() >= 10) {
            throw new RegraNegocioException("Um projeto pode ter no máximo 10 membros.");
        }

        long projetosAtivos = projetoRepository.countProjetosAtivosPorMembro(membroId, List.of(StatusProjeto.ENCERRADO, StatusProjeto.CANCELADO));
        if (projetosAtivos >= 3 && !projeto.getMembrosIds().contains(membroId)) {
            throw new RegraNegocioException("O membro já está alocado no limite máximo de 3 projetos ativos.");
        }

        projeto.getMembrosIds().add(membroId);
        return projetoRepository.save(projeto);
    }

    public RelatorioPortfolioDTO gerarRelatorio() {

        List<Projeto> projetos = projetoRepository.findAll();

        Map<String, Long> quantidadePorStatus = projetos.stream()
                .collect(Collectors.groupingBy(p -> p.getStatus().name(), Collectors.counting()));

        Map<String, BigDecimal> orcamentoPorStatus = projetos.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getStatus().name(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Projeto::getOrcamentoTotal,
                                BigDecimal::add
                        )
                ));

        double mediaDuracao = projetos.stream()
                .filter(p -> p.getStatus() == StatusProjeto.ENCERRADO)
                .mapToLong(p -> ChronoUnit.DAYS.between(p.getDataInicio(), p.getDataRealTermino()))
                .average()
                .orElse(0);

        long membrosUnicos = projetos.stream()
                .flatMap(p -> p.getMembrosIds().stream())
                .distinct()
                .count();

        return new RelatorioPortfolioDTO(
                quantidadePorStatus,
                orcamentoPorStatus,
                mediaDuracao,
                membrosUnicos
        );
    }
}