package com.desafio.lucas.service;

import com.desafio.lucas.dto.MembroMockDTO;
import com.desafio.lucas.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class MockMembroService {

    private final Map<Long, MembroMockDTO> dbMock = new HashMap<>();

    public MockMembroService() {
        dbMock.put(1L, new MembroMockDTO(1L, "João da Silva", "funcionário"));
        dbMock.put(2L, new MembroMockDTO(2L, "Maria Souza", "gerente"));
        dbMock.put(3L, new MembroMockDTO(3L, "Carlos Lima", "funcionário"));
    }

    public MembroMockDTO buscarMembro(Long id) {
        if (!dbMock.containsKey(id)) {
            throw new RecursoNaoEncontradoException("Membro não encontrado na API externa.");
        }
        return dbMock.get(id);
    }
}