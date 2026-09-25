package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.SolicitacaoAlteracaoAluno;
import br.com.mod.gestaomusical.entity.StatusSolicitacaoAlteracaoAluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitacaoAlteracaoAlunoRepository
        extends JpaRepository<SolicitacaoAlteracaoAluno, Long> {

    List<SolicitacaoAlteracaoAluno>
    findAllByOrderByCriadoEmDesc();

    List<SolicitacaoAlteracaoAluno>
    findByStatusOrderByCriadoEmDesc(
            StatusSolicitacaoAlteracaoAluno status
    );

    boolean existsByAluno_IdAndStatus(
            Long alunoId,
            StatusSolicitacaoAlteracaoAluno status
    );

    long countByStatus(
            StatusSolicitacaoAlteracaoAluno status
    );
}