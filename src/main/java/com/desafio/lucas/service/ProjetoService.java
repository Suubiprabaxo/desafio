package com.desafio.lucas.service;

import com.desafio.lucas.dto.MembroMockDTO;
import com.desafio.lucas.exception.RecursoNaoEncontradoException;
import com.desafio.lucas.exception.RegraNegocioException;
import com.desafio.lucas.model.Projeto;
import com.desafio.lucas.model.enums.StatusProjeto;
import com.desafio.lucas.repository.ProjetoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final MockMembroService membroService;

    public Page<Projeto> listar(Pageable pageable) {
        return projetoRepository.findAll(pageable);
    }

    public Projeto buscarPorId(Long id) {
        return projetoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Projeto não encontrado"));
    }

    @Transactional
    public Projeto salvar(Projeto projeto) {
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
        MembroMockDTO membro = membroService.buscarMembro(membroId);

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
}