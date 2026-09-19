package br.com.mod.gestaomusical.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class RelatorioMensalCalculoRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Object[] calcular(LocalDate periodoInicio, LocalDate periodoFim) {

        String sql = """
                WITH movimentacoes AS (

                    SELECT aluno_id, data FROM mts

                    UNION ALL

                    SELECT aluno_id, data FROM msa

                    UNION ALL

                    SELECT aluno_id, data FROM metodo

                    UNION ALL

                    SELECT aluno_id, data FROM hinario

                    UNION ALL

                    SELECT aluno_id, data FROM escala
                ),

                ultima_movimentacao AS (
                    SELECT
                        aluno_id,
                        MAX(data) AS ultima_movimentacao
                    FROM movimentacoes
                    GROUP BY aluno_id
                )

                SELECT

                    (
                        SELECT COUNT(*)
                        FROM aluno
                        WHERE situacao = 'ATIVO'
                    ) AS total_alunos_ativos,

                    (
                        SELECT COUNT(*)
                        FROM aluno
                        WHERE situacao = 'ARQUIVADO'
                    ) AS total_alunos_arquivados,

                    (
                        SELECT COUNT(*)
                        FROM mts
                        WHERE data BETWEEN :periodoInicio AND :periodoFim
                    ) AS total_mts,

                    (
                        SELECT COUNT(*)
                        FROM msa
                        WHERE data BETWEEN :periodoInicio AND :periodoFim
                    ) AS total_msa,

                    (
                        SELECT COUNT(*)
                        FROM metodo
                        WHERE data BETWEEN :periodoInicio AND :periodoFim
                    ) AS total_metodo,

                    (
                        SELECT COUNT(*)
                        FROM hinario
                        WHERE data BETWEEN :periodoInicio AND :periodoFim
                    ) AS total_hinario,

                    (
                        SELECT COUNT(*)
                        FROM escala
                        WHERE data BETWEEN :periodoInicio AND :periodoFim
                    ) AS total_escala,

                    (
                        SELECT COUNT(*)
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
                          ) >= 60
                    ) AS total_alunos_sem_movimentacao_60_dias,

                    (
                        SELECT COUNT(*)
                        FROM aluno_compartilhamento
                        WHERE criado_em >= :periodoInicio
                          AND criado_em < CAST(:periodoFim AS date)
                                           + INTERVAL '1 day'
                    ) AS total_compartilhamentos,

                    (
                        SELECT COUNT(*)
                        FROM exclusao_aluno
                        WHERE data_hora >= :periodoInicio
                          AND data_hora < CAST(:periodoFim AS date)
                                         + INTERVAL '1 day'
                    ) AS total_exclusoes_arquivamentos,

                    (
                        SELECT COUNT(*)
                        FROM auditoria
                        WHERE acao = 'RESTAURACAO_ALUNO'
                          AND criado_em >= :periodoInicio
                          AND criado_em < CAST(:periodoFim AS date)
                                         + INTERVAL '1 day'
                    ) AS total_restauracoes,

                    (
                        SELECT COUNT(*)
                        FROM notificacao
                        WHERE data_hora >= :periodoInicio
                          AND data_hora < CAST(:periodoFim AS date)
                                         + INTERVAL '1 day'
                    ) AS total_notificacoes,

                    (
                        SELECT COUNT(*)
                        FROM auditoria
                        WHERE criado_em >= :periodoInicio
                          AND criado_em < CAST(:periodoFim AS date)
                                         + INTERVAL '1 day'
                    ) AS total_eventos_auditoria
                """;

        return (Object[]) entityManager
                .createNativeQuery(sql)
                .setParameter("periodoInicio", periodoInicio)
                .setParameter("periodoFim", periodoFim)
                .getSingleResult();
    }
}