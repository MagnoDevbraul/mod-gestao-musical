package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.ExclusaoAlunoRequestDTO;
import br.com.mod.gestaomusical.dto.ExclusaoAlunoResponseDTO;
import br.com.mod.gestaomusical.entity.Aluno;
import br.com.mod.gestaomusical.entity.ExclusaoAluno;
import br.com.mod.gestaomusical.entity.Historico;
import br.com.mod.gestaomusical.entity.Notificacao;
import br.com.mod.gestaomusical.entity.Usuario;
import br.com.mod.gestaomusical.repository.AlunoRepository;
import br.com.mod.gestaomusical.repository.ExclusaoAlunoRepository;
import br.com.mod.gestaomusical.repository.HistoricoRepository;
import br.com.mod.gestaomusical.repository.NotificacaoRepository;
import br.com.mod.gestaomusical.security.UsuarioAutenticadoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ExclusaoAlunoService {

    private final ExclusaoAlunoRepository exclusaoAlunoRepository;
    private final AlunoRepository alunoRepository;
    private final HistoricoRepository historicoRepository;
    private final NotificacaoRepository notificacaoRepository;
    private final AuditoriaService auditoriaService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public ExclusaoAlunoService(
            ExclusaoAlunoRepository exclusaoAlunoRepository,
            AlunoRepository alunoRepository,
            HistoricoRepository historicoRepository,
            NotificacaoRepository notificacaoRepository,
            AuditoriaService auditoriaService,
            UsuarioAutenticadoService usuarioAutenticadoService) {

        this.exclusaoAlunoRepository = exclusaoAlunoRepository;
        this.alunoRepository = alunoRepository;
        this.historicoRepository = historicoRepository;
        this.notificacaoRepository = notificacaoRepository;
        this.auditoriaService = auditoriaService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @Transactional(readOnly = true)
    public List<ExclusaoAlunoResponseDTO> listarTodos() {

        return exclusaoAlunoRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<ExclusaoAlunoResponseDTO> buscarPorId(Long id) {

        return exclusaoAlunoRepository.findById(id)
                .map(this::converterParaDTO);
    }

    @Transactional
    public ExclusaoAlunoResponseDTO excluir(
            ExclusaoAlunoRequestDTO dto) {

        if (dto.getAlunoId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Aluno é obrigatório"
            );
        }

        if (dto.getMotivo() == null
                || dto.getMotivo().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Motivo da exclusão é obrigatório"
            );
        }

        Aluno aluno = alunoRepository
                .findById(dto.getAlunoId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Aluno não encontrado"
                        ));

        if ("ARQUIVADO".equalsIgnoreCase(
                aluno.getSituacao())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Aluno já está arquivado"
            );
        }

        /*
         * Usuário real que executou a operação.
         * Obtido diretamente da autenticação.
         */
        Usuario usuario =
                usuarioAutenticadoService
                        .obterUsuarioAutenticado();

        String situacaoAnterior =
                aluno.getSituacao();

        /*
         * 1. Arquivamento lógico do aluno.
         */
        aluno.setSituacao("ARQUIVADO");

        alunoRepository.save(aluno);

        /*
         * 2. Registro formal da exclusão.
         */
        ExclusaoAluno exclusao =
                new ExclusaoAluno();

        exclusao.setAluno(aluno);
        exclusao.setUsuario(usuario);
        exclusao.setMotivo(
                dto.getMotivo().trim()
        );

        ExclusaoAluno exclusaoSalva =
                exclusaoAlunoRepository
                        .save(exclusao);

        /*
         * 3. Histórico.
         */
        Historico historico =
                new Historico();

        historico.setAluno(aluno);
        historico.setUsuario(usuario);
        historico.setTipoEvento(
                "EXCLUSAO_ALUNO"
        );

        historico.setDescricao(
                "Aluno arquivado conforme registro de exclusão no MOD."
        );

        historico.setValorAnterior(
                situacaoAnterior
        );

        historico.setValorNovo(
                "ARQUIVADO"
        );

        historicoRepository.save(historico);

        /*
         * 4. Notificação.
         */
        Notificacao notificacao =
                new Notificacao();

        notificacao.setUsuario(usuario);
        notificacao.setAluno(aluno);
        notificacao.setTipoEvento(
                "EXCLUSAO_ALUNO"
        );

        notificacao.setTitulo(
                "Aluno arquivado"
        );

        notificacao.setMensagem(
                "O aluno "
                        + aluno.getNome()
                        + " foi arquivado no MOD."
        );

        notificacao.setLida(false);
        notificacao.setDataLeitura(null);

        notificacaoRepository.save(notificacao);

        /*
         * 5. Auditoria.
         * O AuditoriaService registra automaticamente
         * o usuário autenticado.
         */
        auditoriaService.registrar(
                "EXCLUSAO_ALUNO",
                "aluno",
                aluno.getId(),
                "Aluno arquivado no MOD por solicitação de exclusão.",
                Map.of(
                        "nome",
                        aluno.getNome(),
                        "situacao",
                        situacaoAnterior
                ),
                Map.of(
                        "nome",
                        aluno.getNome(),
                        "situacao",
                        "ARQUIVADO",
                        "motivo",
                        dto.getMotivo().trim()
                )
        );

        return converterParaDTO(
                exclusaoSalva
        );
    }

    private ExclusaoAlunoResponseDTO converterParaDTO(
            ExclusaoAluno exclusao) {

        ExclusaoAlunoResponseDTO dto =
                new ExclusaoAlunoResponseDTO();

        dto.setId(
                exclusao.getId()
        );

        if (exclusao.getAluno() != null) {

            dto.setAlunoId(
                    exclusao.getAluno().getId()
            );

            dto.setAlunoNome(
                    exclusao.getAluno().getNome()
            );
        }

        if (exclusao.getUsuario() != null) {

            dto.setUsuarioId(
                    exclusao.getUsuario().getId()
            );

            dto.setUsuarioNome(
                    exclusao.getUsuario().getNome()
            );
        }

        dto.setMotivo(
                exclusao.getMotivo()
        );

        dto.setDataHora(
                exclusao.getDataHora()
        );

        return dto;
    }
}