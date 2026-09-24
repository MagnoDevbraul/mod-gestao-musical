package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.AtualizarPermissoesPerfilRequestDTO;
import br.com.mod.gestaomusical.dto.PermissaoResponseDTO;
import br.com.mod.gestaomusical.entity.PerfilUsuario;
import br.com.mod.gestaomusical.entity.Permissao;
import br.com.mod.gestaomusical.repository.PerfilUsuarioRepository;
import br.com.mod.gestaomusical.repository.PermissaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissaoService {

    private final PermissaoRepository permissaoRepository;
    private final PerfilUsuarioRepository perfilUsuarioRepository;
    private final AuditoriaService auditoriaService;

    public PermissaoService(
            PermissaoRepository permissaoRepository,
            PerfilUsuarioRepository perfilUsuarioRepository,
            AuditoriaService auditoriaService) {

        this.permissaoRepository =
                permissaoRepository;

        this.perfilUsuarioRepository =
                perfilUsuarioRepository;

        this.auditoriaService =
                auditoriaService;
    }

    @Transactional(readOnly = true)
    public List<PermissaoResponseDTO> listarTodas() {

        return permissaoRepository
                .findAllByOrderByNomeAsc()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PermissaoResponseDTO> listarPorPerfil(
            Long perfilId) {

        PerfilUsuario perfil =
                perfilUsuarioRepository
                        .findComPermissoesById(perfilId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Perfil de usuário não encontrado"
                                )
                        );

        return perfil.getPermissoes()
                .stream()
                .sorted(
                        (a, b) ->
                                a.getNome()
                                        .compareToIgnoreCase(
                                                b.getNome()
                                        )
                )
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional
    public List<PermissaoResponseDTO> atualizarPermissoesPerfil(
            Long perfilId,
            AtualizarPermissoesPerfilRequestDTO dto) {

        if (dto == null
                || dto.getPermissaoIds() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Lista de permissões é obrigatória"
            );
        }

        PerfilUsuario perfil =
                perfilUsuarioRepository
                        .findComPermissoesById(perfilId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Perfil de usuário não encontrado"
                                )
                        );

        Set<Long> anteriores =
                perfil.getPermissoes()
                        .stream()
                        .map(Permissao::getId)
                        .collect(Collectors.toSet());

        List<Permissao> encontradas =
                permissaoRepository
                        .findAllById(
                                dto.getPermissaoIds()
                        );

        if (encontradas.size()
                != dto.getPermissaoIds().size()) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Uma ou mais permissões não foram encontradas"
            );
        }

        Set<Permissao> novas =
                encontradas
                        .stream()
                        .collect(Collectors.toSet());

        perfil.setPermissoes(
                novas
        );

        perfilUsuarioRepository.save(
                perfil
        );

        Map<String, Object> anterior =
                new LinkedHashMap<>();

        anterior.put(
                "permissaoIds",
                anteriores
        );

        Map<String, Object> novo =
                new LinkedHashMap<>();

        novo.put(
                "permissaoIds",
                novas.stream()
                        .map(Permissao::getId)
                        .collect(Collectors.toSet())
        );

        auditoriaService.registrar(
                "ALTERACAO_PERMISSOES_PERFIL",
                "perfil_usuario",
                perfil.getId(),
                "Permissões do perfil alteradas no MOD.",
                anterior,
                novo
        );

        return novas.stream()
                .sorted(
                        (a, b) ->
                                a.getNome()
                                        .compareToIgnoreCase(
                                                b.getNome()
                                        )
                )
                .map(this::converterParaDTO)
                .toList();
    }

    private PermissaoResponseDTO converterParaDTO(
            Permissao permissao) {

        PermissaoResponseDTO dto =
                new PermissaoResponseDTO();

        dto.setId(
                permissao.getId()
        );

        dto.setNome(
                permissao.getNome()
        );

        dto.setDescricao(
                permissao.getDescricao()
        );

        dto.setAtivo(
                permissao.getAtivo()
        );

        return dto;
    }
}