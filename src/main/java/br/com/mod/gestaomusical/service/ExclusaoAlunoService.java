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

    /*
     * Fluxo utilizado quando a exclusão ocorre no SAM.
     *
     * Secretaria, Encarregado Regional e Encarregado Local
     * poderão executar essa operação.
     *
     * O aluno não é apagado fisicamente do MOD.
     * Ele permanece com a situação ARQUIVADO para
     * preservação de histórico, auditoria e registros musicais.
     */
    @Transactional
    public ExclusaoAlunoResponseDTO excluirNoSam(
            ExclusaoAlunoRequestDTO dto) {

        return arquivar(
                dto,
                "EXCLUSAO_ALUNO_SAM",
                "Aluno arquivado no MOD após exclusão no SAM.",
                "Aluno excluído do SAM",
                "Aluno arquivado no MOD após exclusão no SAM."
        );
    }

    /*
     * Fluxo de arquivamento direto no MOD.
     *
     * Somente a Secretaria possui autorização
     * para executar essa operação.
     */
    @Transactional
    public ExclusaoAlunoResponseDTO arquivarNoMod(
            ExclusaoAlunoRequestDTO dto) {

        return arquivar(
                dto,
                "ARQUIVAMENTO_ALUNO_MOD",
                "Aluno arquivado diretamente no MOD.",
                "Aluno arquivado no MOD",
                "Aluno arquivado diretamente no MOD."
        );
    }

    /*
     * Implementação comum aos dois fluxos.
     *
     * A origem da operação muda, mas o resultado
     * dentro do MOD é o mesmo:
     *
     * situacao = ARQUIVADO
     */
    private ExclusaoAlunoResponseDTO arquivar(
            ExclusaoAlunoRequestDTO dto,
            String tipoEvento,
            String descricaoHistorico,
            String tituloNotificacao,
            String descricaoAuditoria) {

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
                        )
                );

        if ("ARQUIVADO".equalsIgnoreCase(
                aluno.getSituacao())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Aluno já está arquivado"
            );
        }

        /*
         * Usuário real que executou a operação.
         * Obtido diretamente da sessão autenticada.
         */
        Usuario usuario =
                usuarioAutenticadoService
                        .obterUsuarioAutenticado();

        String situacaoAnterior =
                aluno.getSituacao();

        String motivo =
                dto.getMotivo().trim();

        /*
         * 1. Arquivamento lógico do aluno no MOD.
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
        exclusao.setMotivo(motivo);

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
        historico.setTipoEvento(tipoEvento);

        historico.setDescricao(
                descricaoHistorico
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
        notificacao.setTipoEvento(tipoEvento);

        notificacao.setTitulo(
                tituloNotificacao
        );

        notificacao.setMensagem(
                "O aluno "
                        + aluno.getNome()
                        + " foi arquivado no MOD. Motivo: "
                        + motivo
        );

        notificacao.setLida(false);
        notificacao.setDataLeitura(null);

        notificacaoRepository.save(notificacao);

        /*
         * 5. Auditoria.
         *
         * O AuditoriaService registra automaticamente
         * o usuário autenticado.
         */
        auditoriaService.registrar(
                tipoEvento,
                "aluno",
                aluno.getId(),
                descricaoAuditoria,
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
                        motivo
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