package com.desafio.lucas.service;

import com.desafio.lucas.dto.MembroDTO;
import com.desafio.lucas.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class MockMembroService {

    private final Map<Long, MembroDTO> dbMock = new HashMap<>();

    public MockMembroService() {
        dbMock.put(1L, new MembroDTO(1L, "João da Silva", "funcionário"));
        dbMock.put(2L, new MembroDTO(2L, "Maria Souza", "gerente"));
        dbMock.put(3L, new MembroDTO(3L, "Carlos Lima", "funcionário"));
    }

    public MembroDTO buscarMembro(Long id) {
        if (!dbMock.containsKey(id)) {
            throw new RecursoNaoEncontradoException("Membro não encontrado na API externa.");
        }
        return dbMock.get(id);
    }
}