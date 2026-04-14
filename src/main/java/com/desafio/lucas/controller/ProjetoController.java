package com.desafio.lucas.controller;

import com.desafio.lucas.dto.ProjetoCriacaoDTO;
import com.desafio.lucas.dto.ProjetoDTO;
import com.desafio.lucas.dto.RelatorioPortfolioDTO;
import com.desafio.lucas.mapper.ProjetoMapper;
import com.desafio.lucas.model.enums.StatusProjeto;
import com.desafio.lucas.service.ProjetoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    private final ProjetoMapper projetoMapper;

    @GetMapping
    @Operation(summary = "Listar projetos com paginação")
    public ResponseEntity<Page<ProjetoDTO>> listar(
            @RequestParam(required = false) StatusProjeto status,
            Pageable pageable) {

        Page<ProjetoDTO> projetos = projetoService.listar(status, pageable)
                .map(projetoMapper::toDTO);

        return ResponseEntity.ok(projetos);
    }

    @PostMapping
    @Operation(summary = "Criar um novo projeto")
    public ResponseEntity<ProjetoDTO> criar(@RequestBody @Valid ProjetoCriacaoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projetoMapper.toDTO(projetoService.salvar(dto)));
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
        return ResponseEntity.ok(projetoMapper.toDTO(projetoService.alterarStatus(id, novoStatus)));
    }

    @PostMapping("/{projetoId}/membros/{membroId}")
    @Operation(summary = "Associar membro ao projeto")
    public ResponseEntity<ProjetoDTO> associarMembro(@PathVariable Long projetoId, @PathVariable Long membroId) {
        return ResponseEntity.ok(projetoMapper.toDTO(projetoService.associarMembro(projetoId, membroId)));
    }

    @GetMapping("/relatorio")
    public ResponseEntity<RelatorioPortfolioDTO> relatorio() {
        return ResponseEntity.ok(projetoService.gerarRelatorio());
    }
}