package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.AlterarUsuarioAtivoRequestDTO;
import br.com.mod.gestaomusical.dto.AlterarUsuarioPerfilRequestDTO;
import br.com.mod.gestaomusical.dto.UsuarioResponseDTO;
import br.com.mod.gestaomusical.entity.PerfilUsuario;
import br.com.mod.gestaomusical.entity.Usuario;
import br.com.mod.gestaomusical.repository.PerfilUsuarioRepository;
import br.com.mod.gestaomusical.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilUsuarioRepository perfilUsuarioRepository;
    private final AuditoriaService auditoriaService;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PerfilUsuarioRepository perfilUsuarioRepository,
            AuditoriaService auditoriaService) {

        this.usuarioRepository = usuarioRepository;
        this.perfilUsuarioRepository = perfilUsuarioRepository;
        this.auditoriaService = auditoriaService;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {

        return usuarioRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioResponseDTO> buscarPorId(
            Long id) {

        return usuarioRepository.findById(id)
                .map(this::converterParaDTO);
    }

    @Transactional
    public UsuarioResponseDTO alterarAtivo(
            Long usuarioId,
            AlterarUsuarioAtivoRequestDTO dto) {

        if (dto == null || dto.getAtivo() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Situação ativa do usuário é obrigatória"
            );
        }

        Usuario usuario = buscarUsuario(usuarioId);

        Boolean ativoAnterior =
                usuario.getAtivo();

        usuario.setAtivo(
                dto.getAtivo()
        );

        usuario.setAtualizadoEm(
                LocalDateTime.now()
        );

        Usuario salvo =
                usuarioRepository.save(usuario);

        Map<String, Object> anterior =
                new LinkedHashMap<>();

        anterior.put(
                "ativo",
                ativoAnterior
        );

        Map<String, Object> novo =
                new LinkedHashMap<>();

        novo.put(
                "ativo",
                salvo.getAtivo()
        );

        auditoriaService.registrar(
                "ALTERACAO_STATUS_USUARIO",
                "usuario",
                salvo.getId(),
                "Situação ativa do usuário alterada no MOD.",
                anterior,
                novo
        );

        return converterParaDTO(salvo);
    }

    @Transactional
    public UsuarioResponseDTO alterarPerfil(
            Long usuarioId,
            AlterarUsuarioPerfilRequestDTO dto) {

        if (dto == null
                || dto.getPerfilUsuarioId() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Perfil do usuário é obrigatório"
            );
        }

        Usuario usuario =
                buscarUsuario(usuarioId);

        PerfilUsuario perfilNovo =
                perfilUsuarioRepository
                        .findById(
                                dto.getPerfilUsuarioId()
                        )
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Perfil de usuário não encontrado"
                                )
                        );

        if (!Boolean.TRUE.equals(
                perfilNovo.getAtivo())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Perfil de usuário está inativo"
            );
        }

        Long perfilAnteriorId =
                usuario.getPerfilUsuario() != null
                        ? usuario.getPerfilUsuario().getId()
                        : null;

        String perfilAnteriorNome =
                usuario.getPerfilUsuario() != null
                        ? usuario.getPerfilUsuario().getNome()
                        : null;

        usuario.setPerfilUsuario(
                perfilNovo
        );

        usuario.setAtualizadoEm(
                LocalDateTime.now()
        );

        Usuario salvo =
                usuarioRepository.save(usuario);

        Map<String, Object> anterior =
                new LinkedHashMap<>();

        anterior.put(
                "perfilUsuarioId",
                perfilAnteriorId
        );

        anterior.put(
                "perfilUsuarioNome",
                perfilAnteriorNome
        );

        Map<String, Object> novo =
                new LinkedHashMap<>();

        novo.put(
                "perfilUsuarioId",
                perfilNovo.getId()
        );

        novo.put(
                "perfilUsuarioNome",
                perfilNovo.getNome()
        );

        auditoriaService.registrar(
                "ALTERACAO_PERFIL_USUARIO",
                "usuario",
                salvo.getId(),
                "Perfil do usuário alterado no MOD.",
                anterior,
                novo
        );

        return converterParaDTO(salvo);
    }

    private Usuario buscarUsuario(
            Long id) {

        return usuarioRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Usuário não encontrado"
                        )
                );
    }

    private UsuarioResponseDTO converterParaDTO(
            Usuario usuario) {

        UsuarioResponseDTO dto =
                new UsuarioResponseDTO();

        dto.setId(
                usuario.getId()
        );

        dto.setNome(
                usuario.getNome()
        );

        dto.setEmail(
                usuario.getEmail()
        );

        dto.setAtivo(
                usuario.getAtivo()
        );

        if (usuario.getPerfilUsuario() != null) {

            dto.setPerfilUsuarioId(
                    usuario.getPerfilUsuario().getId()
            );

            dto.setPerfilUsuarioNome(
                    usuario.getPerfilUsuario().getNome()
            );
        }

        if (usuario.getComum() != null) {

            dto.setComumId(
                    usuario.getComum().getId()
            );

            dto.setComumNome(
                    usuario.getComum().getNome()
            );
        }

        return dto;
    }
}