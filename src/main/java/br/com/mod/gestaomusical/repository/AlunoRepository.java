package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlunoRepository
        extends JpaRepository<Aluno, Long> {

    List<Aluno> findBySituacaoIgnoreCase(
            String situacao
    );

    long countBySituacaoIgnoreCase(
            String situacao
    );

    @Query("""
            SELECT
                a.comum.nome AS comum,
                COUNT(a.id) AS quantidade
            FROM Aluno a
            WHERE a.comum IS NOT NULL
            GROUP BY a.comum.nome
            ORDER BY COUNT(a.id) DESC
            """)
    List<AlunosPorComumProjection> contarAlunosPorComum();

    interface AlunosPorComumProjection {

        String getComum();

        Long getQuantidade();
    }
}