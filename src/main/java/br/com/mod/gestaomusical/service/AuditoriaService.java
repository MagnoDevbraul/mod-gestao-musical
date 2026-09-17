package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.AuditoriaResponseDTO;
import br.com.mod.gestaomusical.entity.Auditoria;
import br.com.mod.gestaomusical.repository.AuditoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaService(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<AuditoriaResponseDTO> listarTodos() {
        return auditoriaRepository.findAll().stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<AuditoriaResponseDTO> buscarPorId(Long id) {
        return auditoriaRepository.findById(id)
                .map(this::converterParaDTO);
    }

    private AuditoriaResponseDTO converterParaDTO(Auditoria auditoria) {

        AuditoriaResponseDTO dto = new AuditoriaResponseDTO();

        dto.setId(auditoria.getId());

        if (auditoria.getUsuario() != null) {
            dto.setUsuarioId(auditoria.getUsuario().getId());
            dto.setUsuarioNome(auditoria.getUsuario().getNome());
        }

        dto.setAcao(auditoria.getAcao());
        dto.setTabelaAfetada(auditoria.getTabelaAfetada());
        dto.setRegistroId(auditoria.getRegistroId());
        dto.setDescricao(auditoria.getDescricao());
        dto.setDadosAnteriores(auditoria.getDadosAnteriores());
        dto.setDadosNovos(auditoria.getDadosNovos());
        dto.setCriadoEm(auditoria.getCriadoEm());

        return dto;
    }
}