package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.DashboardAlunosPorComumResponseDTO;
import br.com.mod.gestaomusical.dto.DashboardAtividadeResponseDTO;
import br.com.mod.gestaomusical.dto.DashboardResponseDTO;
import br.com.mod.gestaomusical.entity.Historico;
import br.com.mod.gestaomusical.entity.StatusSolicitacaoAlteracaoAluno;
import br.com.mod.gestaomusical.repository.AlunoRepository;
import br.com.mod.gestaomusical.repository.HistoricoRepository;
import br.com.mod.gestaomusical.repository.NotificacaoRepository;
import br.com.mod.gestaomusical.repository.SolicitacaoAlteracaoAlunoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DashboardService {

    private final AlunoRepository alunoRepository;

    private final SolicitacaoAlteracaoAlunoRepository
            solicitacaoAlteracaoAlunoRepository;

    private final NotificacaoRepository notificacaoRepository;

    private final HistoricoRepository historicoRepository;

    public DashboardService(
            AlunoRepository alunoRepository,
            SolicitacaoAlteracaoAlunoRepository
                    solicitacaoAlteracaoAlunoRepository,
            NotificacaoRepository notificacaoRepository,
            HistoricoRepository historicoRepository) {

        this.alunoRepository =
                alunoRepository;

        this.solicitacaoAlteracaoAlunoRepository =
                solicitacaoAlteracaoAlunoRepository;

        this.notificacaoRepository =
                notificacaoRepository;

        this.historicoRepository =
                historicoRepository;
    }

    @Transactional(readOnly = true)
    public DashboardResponseDTO obterDashboardSecretaria() {

        DashboardResponseDTO dto =
                new DashboardResponseDTO();

        dto.setTotalAlunos(
                alunoRepository.count()
        );

        dto.setAlunosAtivos(
                alunoRepository
                        .countBySituacaoIgnoreCase(
                                "ATIVO"
                        )
        );

        dto.setAlunosArquivados(
                alunoRepository
                        .countBySituacaoIgnoreCase(
                                "ARQUIVADO"
                        )
        );

        dto.setAlteracoesPendentes(
                solicitacaoAlteracaoAlunoRepository
                        .countByStatus(
                                StatusSolicitacaoAlteracaoAluno.PENDENTE
                        )
        );

        dto.setNotificacoes(
                notificacaoRepository.count()
        );

        List<DashboardAtividadeResponseDTO>
                atividades =
                historicoRepository
                        .findTop5ByOrderByDataHoraDesc()
                        .stream()
                        .map(this::converterAtividade)
                        .toList();

        dto.setAtividadesRecentes(
                atividades
        );

        List<DashboardAlunosPorComumResponseDTO>
                alunosPorComum =
                alunoRepository
                        .contarAlunosPorComum()
                        .stream()
                        .map(item ->
                                new DashboardAlunosPorComumResponseDTO(
                                        item.getComum(),
                                        item.getQuantidade()
                                )
                        )
                        .toList();

        dto.setAlunosPorComum(
                alunosPorComum
        );

        return dto;
    }

    private DashboardAtividadeResponseDTO
    converterAtividade(
            Historico historico) {

        DashboardAtividadeResponseDTO dto =
                new DashboardAtividadeResponseDTO();

        dto.setId(
                historico.getId()
        );

        dto.setTipoEvento(
                historico.getTipoEvento()
        );

        dto.setDescricao(
                historico.getDescricao()
        );

        dto.setDataHora(
                historico.getDataHora()
        );

        return dto;
    }
}