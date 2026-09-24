package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.HistoricoResponseDTO;
import br.com.mod.gestaomusical.entity.Historico;
import br.com.mod.gestaomusical.repository.HistoricoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class HistoricoService {

    private static final List<String> EVENTOS_DESENVOLVIMENTO_MUSICAL =
            List.of(
                    "REGISTRO_MSA",
                    "REGISTRO_MTS",
                    "REGISTRO_METODO",
                    "REGISTRO_HINARIO",
                    "REGISTRO_ESCALA"
            );

    private final HistoricoRepository historicoRepository;

    public HistoricoService(
            HistoricoRepository historicoRepository) {

        this.historicoRepository =
                historicoRepository;
    }

    /*
     * Consulta administrativa completa.
     *
     * O acesso é controlado no Controller
     * pela permissão AUDITORIA_CONSULTAR.
     */
    @Transactional(readOnly = true)
    public List<HistoricoResponseDTO> listarTodos() {

        return historicoRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    /*
     * Consulta administrativa por ID.
     */
    @Transactional(readOnly = true)
    public Optional<HistoricoResponseDTO> buscarPorId(
            Long id) {

        return historicoRepository.findById(id)
                .map(this::converterParaDTO);
    }

    /*
     * Histórico de desenvolvimento musical
     * de um aluno.
     *
     * Retorna somente:
     * - MSA
     * - MTS
     * - Método
     * - Hinário
     * - Escala
     */
    @Transactional(readOnly = true)
    public List<HistoricoResponseDTO> listarDesenvolvimentoAluno(
            Long alunoId) {

        return historicoRepository
                .findByAluno_IdAndTipoEventoInOrderByDataHoraDesc(
                        alunoId,
                        EVENTOS_DESENVOLVIMENTO_MUSICAL
                )
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    private HistoricoResponseDTO converterParaDTO(
            Historico historico) {

        HistoricoResponseDTO dto =
                new HistoricoResponseDTO();

        dto.setId(
                historico.getId()
        );

        if (historico.getAluno() != null) {

            dto.setAlunoId(
                    historico.getAluno().getId()
            );

            dto.setAlunoNome(
                    historico.getAluno().getNome()
            );
        }

        if (historico.getUsuario() != null) {

            dto.setUsuarioId(
                    historico.getUsuario().getId()
            );

            dto.setUsuarioNome(
                    historico.getUsuario().getNome()
            );
        }

        dto.setTipoEvento(
                historico.getTipoEvento()
        );

        dto.setDataHora(
                historico.getDataHora()
        );

        dto.setDescricao(
                historico.getDescricao()
        );

        dto.setValorAnterior(
                historico.getValorAnterior()
        );

        dto.setValorNovo(
                historico.getValorNovo()
        );

        return dto;
    }
}