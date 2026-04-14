package com.desafio.lucas.repository;

import com.desafio.lucas.model.Projeto;
import com.desafio.lucas.model.enums.StatusProjeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

    @Query("SELECT COUNT(p) FROM Projeto p JOIN p.membrosIds m WHERE m = :membroId AND p.status NOT IN (:statusExcluidos)")
    long countProjetosAtivosPorMembro(Long membroId, List<StatusProjeto> statusExcluidos);
}