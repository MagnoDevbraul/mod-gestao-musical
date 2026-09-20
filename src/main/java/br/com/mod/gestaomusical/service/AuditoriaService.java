package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.AuditoriaResponseDTO;
import br.com.mod.gestaomusical.entity.Auditoria;
import br.com.mod.gestaomusical.entity.Usuario;
import br.com.mod.gestaomusical.repository.AuditoriaRepository;
import br.com.mod.gestaomusical.security.UsuarioAutenticadoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public AuditoriaService(
            AuditoriaRepository auditoriaRepository,
            UsuarioAutenticadoService usuarioAutenticadoService) {

        this.auditoriaRepository = auditoriaRepository;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @Transactional
    public Auditoria registrar(
            String acao,
            String tabelaAfetada,
            Long registroId,
            String descricao,
            Map<String, Object> dadosAnteriores,
            Map<String, Object> dadosNovos) {

        Usuario usuario =
                usuarioAutenticadoService.obterUsuarioAutenticado();

        Auditoria auditoria = new Auditoria();

        auditoria.setUsuario(usuario);
        auditoria.setAcao(acao);
        auditoria.setTabelaAfetada(tabelaAfetada);
        auditoria.setRegistroId(registroId);
        auditoria.setDescricao(descricao);
        auditoria.setDadosAnteriores(dadosAnteriores);
        auditoria.setDadosNovos(dadosNovos);

        return auditoriaRepository.save(auditoria);
    }

    @Transactional(readOnly = true)
    public List<AuditoriaResponseDTO> listarTodos() {

        return auditoriaRepository
                .findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<AuditoriaResponseDTO> buscarPorId(Long id) {

        return auditoriaRepository
                .findById(id)
                .map(this::converterParaDTO);
    }

    private AuditoriaResponseDTO converterParaDTO(
            Auditoria auditoria) {

        AuditoriaResponseDTO dto =
                new AuditoriaResponseDTO();

        dto.setId(auditoria.getId());

        if (auditoria.getUsuario() != null) {
            dto.setUsuarioId(
                    auditoria.getUsuario().getId()
            );

            dto.setUsuarioNome(
                    auditoria.getUsuario().getNome()
            );
        }

        dto.setAcao(
                auditoria.getAcao()
        );

        dto.setTabelaAfetada(
                auditoria.getTabelaAfetada()
        );

        dto.setRegistroId(
                auditoria.getRegistroId()
        );

        dto.setDescricao(
                auditoria.getDescricao()
        );

        dto.setDadosAnteriores(
                auditoria.getDadosAnteriores()
        );

        dto.setDadosNovos(
                auditoria.getDadosNovos()
        );

        dto.setCriadoEm(
                auditoria.getCriadoEm()
        );

        return dto;
    }
}