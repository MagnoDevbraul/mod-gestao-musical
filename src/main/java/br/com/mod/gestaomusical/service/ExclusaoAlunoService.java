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
import br.com.mod.gestaomusical.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import br.com.mod.gestaomusical.entity.Auditoria;
import br.com.mod.gestaomusical.repository.AuditoriaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
public class ExclusaoAlunoService {

    private final ExclusaoAlunoRepository exclusaoAlunoRepository;
    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;
    private final HistoricoRepository historicoRepository;
    private final NotificacaoRepository notificacaoRepository;
    private final AuditoriaRepository auditoriaRepository;

    public ExclusaoAlunoService(
            ExclusaoAlunoRepository exclusaoAlunoRepository,
            AlunoRepository alunoRepository,
            UsuarioRepository usuarioRepository,
            HistoricoRepository historicoRepository,
            NotificacaoRepository notificacaoRepository,
            AuditoriaRepository auditoriaRepository) {

        this.exclusaoAlunoRepository = exclusaoAlunoRepository;
        this.alunoRepository = alunoRepository;
        this.usuarioRepository = usuarioRepository;
        this.historicoRepository = historicoRepository;
        this.notificacaoRepository = notificacaoRepository;
        this.auditoriaRepository = auditoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<ExclusaoAlunoResponseDTO> listarTodos() {
        return exclusaoAlunoRepository.findAll().stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<ExclusaoAlunoResponseDTO> buscarPorId(Long id) {
        return exclusaoAlunoRepository.findById(id)
                .map(this::converterParaDTO);
    }

    @Transactional
    public ExclusaoAlunoResponseDTO excluir(ExclusaoAlunoRequestDTO dto) {

        if (dto.getMotivo() == null || dto.getMotivo().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Motivo da exclusão é obrigatório"
            );
        }

        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado"
                ));

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado"
                ));

        if ("ARQUIVADO".equalsIgnoreCase(aluno.getSituacao())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Aluno já está arquivado"
            );
        }

        String situacaoAnterior = aluno.getSituacao();

        // 1. Arquiva o aluno
        aluno.setSituacao("ARQUIVADO");
        alunoRepository.save(aluno);

        // 2. Registra a exclusão
        ExclusaoAluno exclusao = new ExclusaoAluno();
        exclusao.setAluno(aluno);
        exclusao.setUsuario(usuario);
        exclusao.setMotivo(dto.getMotivo());

        ExclusaoAluno exclusaoSalva =
                exclusaoAlunoRepository.save(exclusao);

        // 3. Registra automaticamente no histórico
        Historico historico = new Historico();
        historico.setAluno(aluno);
        historico.setUsuario(usuario);
        historico.setTipoEvento("EXCLUSAO_ALUNO");
        historico.setDescricao(
                "Aluno arquivado conforme registro de exclusão no MOD."
        );
        historico.setValorAnterior(situacaoAnterior);
        historico.setValorNovo("ARQUIVADO");

        historicoRepository.save(historico);

        // 4. Gera automaticamente a notificação
        Notificacao notificacao = new Notificacao();
        notificacao.setUsuario(usuario);
        notificacao.setAluno(aluno);
        notificacao.setTipoEvento("EXCLUSAO_ALUNO");
        notificacao.setTitulo("Aluno arquivado");
        notificacao.setMensagem(
                "O aluno " + aluno.getNome() + " foi arquivado no MOD."
        );
        notificacao.setLida(false);
        notificacao.setDataLeitura(null);

        notificacaoRepository.save(notificacao);

        Auditoria auditoria = new Auditoria();

        auditoria.setUsuario(usuario);
        auditoria.setAcao("EXCLUSAO_ALUNO");
        auditoria.setTabelaAfetada("aluno");
        auditoria.setRegistroId(aluno.getId());
        auditoria.setDescricao(
                "Aluno arquivado no MOD por solicitação de exclusão."
        );

        auditoria.setDadosAnteriores(
                Map.of(
                        "nome", aluno.getNome(),
                        "situacao", situacaoAnterior
                )
        );

        auditoria.setDadosNovos(
                Map.of(
                        "nome", aluno.getNome(),
                        "situacao", "ARQUIVADO"
                )
        );

        auditoriaRepository.save(auditoria);

        return converterParaDTO(exclusaoSalva);
    }

    private ExclusaoAlunoResponseDTO converterParaDTO(
            ExclusaoAluno exclusao) {

        ExclusaoAlunoResponseDTO dto =
                new ExclusaoAlunoResponseDTO();

        dto.setId(exclusao.getId());

        if (exclusao.getAluno() != null) {
            dto.setAlunoId(exclusao.getAluno().getId());
            dto.setAlunoNome(exclusao.getAluno().getNome());
        }

        if (exclusao.getUsuario() != null) {
            dto.setUsuarioId(exclusao.getUsuario().getId());
            dto.setUsuarioNome(exclusao.getUsuario().getNome());
        }

        dto.setMotivo(exclusao.getMotivo());
        dto.setDataHora(exclusao.getDataHora());

        return dto;
    }
}