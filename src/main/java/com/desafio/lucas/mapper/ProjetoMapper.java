package com.desafio.lucas.mapper;

import com.desafio.lucas.dto.ProjetoCriacaoDTO;
import com.desafio.lucas.dto.ProjetoDTO;
import com.desafio.lucas.model.Projeto;
import org.springframework.stereotype.Component;

@Component
public class ProjetoMapper {

    public Projeto toEntity(ProjetoCriacaoDTO dto) {
        Projeto p = new Projeto();
        p.setNome(dto.nome());
        p.setDataInicio(dto.dataInicio());
        p.setPrevisaoTermino(dto.previsaoTermino());
        p.setDataRealTermino(dto.dataRealTermino());
        p.setOrcamentoTotal(dto.orcamentoTotal());
        p.setDescricao(dto.descricao());
        p.setIdGerente(dto.idGerente());
        return p;
    }

    public ProjetoDTO toDTO(Projeto p) {
        return new ProjetoDTO(
                p.getId(),
                p.getNome(),
                p.getDataInicio(),
                p.getPrevisaoTermino(),
                p.getDataRealTermino(),
                p.getOrcamentoTotal(),
                p.getDescricao(),
                p.getIdGerente(),
                p.getStatus(),
                p.getRisco(),
                p.getMembrosIds()
        );
    }
}