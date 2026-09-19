package br.com.mod.gestaomusical.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AlunoSemMovimentacaoRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> buscarAlunosSemMovimentacao(int dias) {

        String sql = """
                WITH movimentacoes AS (

                    SELECT aluno_id, data
                    FROM mts

                    UNION ALL

                    SELECT aluno_id, data
                    FROM msa

                    UNION ALL

                    SELECT aluno_id, data
                    FROM metodo

                    UNION ALL

                    SELECT aluno_id, data
                    FROM hinario

                    UNION ALL

                    SELECT aluno_id, data
                    FROM escala
                ),

                ultima_movimentacao AS (
                    SELECT
                        aluno_id,
                        MAX(data) AS ultima_movimentacao
                    FROM movimentacoes
                    GROUP BY aluno_id
                )

                SELECT
                    a.id AS aluno_id,
                    a.nome AS aluno_nome,
                    um.ultima_movimentacao,
                    a.data_inicio_gem,

                    COALESCE(
                        um.ultima_movimentacao,
                        a.data_inicio_gem,
                        CAST(a.criado_em AS date)
                    ) AS data_base,

                    CURRENT_DATE -
                    COALESCE(
                        um.ultima_movimentacao,
                        a.data_inicio_gem,
                        CAST(a.criado_em AS date)
                    ) AS dias_sem_movimentacao

                FROM aluno a

                LEFT JOIN ultima_movimentacao um
                    ON um.aluno_id = a.id

                WHERE a.situacao = 'ATIVO'
                  AND (
                      CURRENT_DATE -
                      COALESCE(
                          um.ultima_movimentacao,
                          a.data_inicio_gem,
                          CAST(a.criado_em AS date)
                      )
                  ) >= :dias

                ORDER BY dias_sem_movimentacao DESC
                """;

        @SuppressWarnings("unchecked")
        List<Object[]> resultado = entityManager
                .createNativeQuery(sql)
                .setParameter("dias", dias)
                .getResultList();

        return resultado;
    }
}