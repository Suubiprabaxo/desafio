package com.desafio.lucas.controller;

import com.desafio.lucas.dto.ProjetoDTO;
import com.desafio.lucas.model.Projeto;
import com.desafio.lucas.model.enums.StatusProjeto;
import com.desafio.lucas.service.ProjetoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projetos")
@RequiredArgsConstructor
@Tag(name = "Projetos", description = "Gerenciamento de Projetos do Portfólio")
public class ProjetoController {

    private final ProjetoService projetoService;

    @GetMapping
    @Operation(summary = "Listar projetos com paginação")
    public ResponseEntity<Page<ProjetoDTO>> listar(Pageable pageable) {
        Page<ProjetoDTO> projetos = projetoService.listar(pageable).map(this::toDTO);
        return ResponseEntity.ok(projetos);
    }

    @PostMapping
    @Operation(summary = "Criar um novo projeto")
    public ResponseEntity<ProjetoDTO> criar(@RequestBody Projeto projeto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(projetoService.salvar(projeto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um projeto")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        projetoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Alterar status do projeto")
    public ResponseEntity<ProjetoDTO> alterarStatus(@PathVariable Long id, @RequestParam StatusProjeto novoStatus) {
        return ResponseEntity.ok(toDTO(projetoService.alterarStatus(id, novoStatus)));
    }

    @PostMapping("/{projetoId}/membros/{membroId}")
    @Operation(summary = "Associar membro ao projeto")
    public ResponseEntity<ProjetoDTO> associarMembro(@PathVariable Long projetoId, @PathVariable Long membroId) {
        return ResponseEntity.ok(toDTO(projetoService.associarMembro(projetoId, membroId)));
    }

    private ProjetoDTO toDTO(Projeto p) {
        return new ProjetoDTO(p.getId(), p.getNome(), p.getDataInicio(), p.getPrevisaoTermino(),
                p.getDataRealTermino(), p.getOrcamentoTotal(), p.getDescricao(), p.getIdGerente(),
                p.getStatus(), p.getRisco(), p.getMembrosIds());
    }
}