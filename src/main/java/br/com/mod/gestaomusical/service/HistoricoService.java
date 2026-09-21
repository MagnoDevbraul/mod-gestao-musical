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

    private final HistoricoRepository historicoRepository;

    public HistoricoService(
            HistoricoRepository historicoRepository) {

        this.historicoRepository = historicoRepository;
    }

    @Transactional(readOnly = true)
    public List<HistoricoResponseDTO> listarTodos() {

        return historicoRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<HistoricoResponseDTO> buscarPorId(
            Long id) {

        return historicoRepository.findById(id)
                .map(this::converterParaDTO);
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