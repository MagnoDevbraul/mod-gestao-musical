package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.AlunoCompartilhamentoRequestDTO;
import br.com.mod.gestaomusical.dto.AlunoCompartilhamentoResponseDTO;
import br.com.mod.gestaomusical.entity.Aluno;
import br.com.mod.gestaomusical.entity.AlunoCompartilhamento;
import br.com.mod.gestaomusical.entity.Auditoria;
import br.com.mod.gestaomusical.entity.ComumCongregacao;
import br.com.mod.gestaomusical.entity.Historico;
import br.com.mod.gestaomusical.entity.Notificacao;
import br.com.mod.gestaomusical.entity.Usuario;
import br.com.mod.gestaomusical.repository.AlunoCompartilhamentoRepository;
import br.com.mod.gestaomusical.repository.AlunoRepository;
import br.com.mod.gestaomusical.repository.AuditoriaRepository;
import br.com.mod.gestaomusical.repository.ComumCongregacaoRepository;
import br.com.mod.gestaomusical.repository.HistoricoRepository;
import br.com.mod.gestaomusical.repository.NotificacaoRepository;
import br.com.mod.gestaomusical.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AlunoCompartilhamentoService {

    private final AlunoCompartilhamentoRepository compartilhamentoRepository;
    private final AlunoRepository alunoRepository;
    private final ComumCongregacaoRepository comumRepository;
    private final UsuarioRepository usuarioRepository;
    private final HistoricoRepository historicoRepository;
    private final NotificacaoRepository notificacaoRepository;
    private final AuditoriaRepository auditoriaRepository;

    public AlunoCompartilhamentoService(
            AlunoCompartilhamentoRepository compartilhamentoRepository,
            AlunoRepository alunoRepository,
            ComumCongregacaoRepository comumRepository,
            UsuarioRepository usuarioRepository,
            HistoricoRepository historicoRepository,
            NotificacaoRepository notificacaoRepository,
            AuditoriaRepository auditoriaRepository) {

        this.compartilhamentoRepository = compartilhamentoRepository;
        this.alunoRepository = alunoRepository;
        this.comumRepository = comumRepository;
        this.usuarioRepository = usuarioRepository;
        this.historicoRepository = historicoRepository;
        this.notificacaoRepository = notificacaoRepository;
        this.auditoriaRepository = auditoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<AlunoCompartilhamentoResponseDTO> listarTodos() {
        return compartilhamentoRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<AlunoCompartilhamentoResponseDTO> buscarPorId(Long id) {
        return compartilhamentoRepository.findById(id)
                .map(this::converterParaDTO);
    }

    @Transactional(readOnly = true)
    public Optional<AlunoCompartilhamentoResponseDTO> buscarPorAlunoId(Long alunoId) {
        return compartilhamentoRepository.findByAlunoId(alunoId)
                .map(this::converterParaDTO);
    }

    @Transactional
    public AlunoCompartilhamentoResponseDTO compartilhar(
            AlunoCompartilhamentoRequestDTO dto) {

        validar(dto);

        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado"
                ));

        if ("ARQUIVADO".equalsIgnoreCase(aluno.getSituacao())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Aluno arquivado não pode ser compartilhado"
            );
        }

        if (compartilhamentoRepository.existsByAlunoId(aluno.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Aluno já possui compartilhamento ativo com outra Comum"
            );
        }

        ComumCongregacao comumDestino = comumRepository
                .findById(dto.getComumDestinoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Comum de destino não encontrada"
                ));

        if (aluno.getComum() != null
                && aluno.getComum().getId().equals(comumDestino.getId())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Aluno não pode ser compartilhado com a própria Comum"
            );
        }

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário responsável pelo compartilhamento não encontrado"
                ));

        AlunoCompartilhamento compartilhamento =
                new AlunoCompartilhamento();

        compartilhamento.setAluno(aluno);
        compartilhamento.setComumDestino(comumDestino);
        compartilhamento.setCompartilhadoPorUsuario(usuario);

        AlunoCompartilhamento salvo =
                compartilhamentoRepository.save(compartilhamento);

        criarHistorico(salvo, aluno, usuario, comumDestino);
        criarNotificacao(salvo, aluno, usuario, comumDestino);
        criarAuditoria(salvo, usuario);

        return converterParaDTO(salvo);
    }

    private void validar(AlunoCompartilhamentoRequestDTO dto) {

        if (dto.getAlunoId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Aluno é obrigatório"
            );
        }

        if (dto.getComumDestinoId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Comum de destino é obrigatória"
            );
        }

        if (dto.getUsuarioId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Usuário responsável pelo compartilhamento é obrigatório"
            );
        }
    }

    private void criarHistorico(
            AlunoCompartilhamento compartilhamento,
            Aluno aluno,
            Usuario usuario,
            ComumCongregacao comumDestino) {

        Historico historico = new Historico();

        historico.setAluno(aluno);
        historico.setUsuario(usuario);
        historico.setTipoEvento("COMPARTILHAMENTO_ALUNO");
        historico.setDescricao(
                "Aluno compartilhado com outra Comum no MOD."
        );

        historico.setValorAnterior(
                aluno.getComum() != null
                        ? aluno.getComum().getNome()
                        : null
        );

        historico.setValorNovo(
                comumDestino.getNome()
        );

        historicoRepository.save(historico);
    }

    private void criarNotificacao(
            AlunoCompartilhamento compartilhamento,
            Aluno aluno,
            Usuario usuario,
            ComumCongregacao comumDestino) {

        Notificacao notificacao = new Notificacao();

        notificacao.setUsuario(usuario);
        notificacao.setAluno(aluno);
        notificacao.setTipoEvento("COMPARTILHAMENTO_ALUNO");
        notificacao.setTitulo("Aluno compartilhado");

        notificacao.setMensagem(
                "O aluno "
                        + aluno.getNome()
                        + " foi compartilhado com a Comum "
                        + comumDestino.getNome()
                        + "."
        );

        notificacao.setLida(false);
        notificacao.setDataLeitura(null);

        notificacaoRepository.save(notificacao);
    }

    private void criarAuditoria(
            AlunoCompartilhamento compartilhamento,
            Usuario usuario) {

        Auditoria auditoria = new Auditoria();

        auditoria.setUsuario(usuario);
        auditoria.setAcao("COMPARTILHAMENTO_ALUNO");
        auditoria.setTabelaAfetada("aluno_compartilhamento");
        auditoria.setRegistroId(compartilhamento.getId());

        auditoria.setDescricao(
                "Compartilhamento de aluno com outra Comum criado no MOD."
        );

        auditoria.setDadosAnteriores(null);
        auditoria.setDadosNovos(
                criarSnapshot(compartilhamento)
        );

        auditoriaRepository.save(auditoria);
    }

    private Map<String, Object> criarSnapshot(
            AlunoCompartilhamento compartilhamento) {

        Map<String, Object> dados = new LinkedHashMap<>();

        dados.put(
                "alunoId",
                compartilhamento.getAluno().getId()
        );

        dados.put(
                "alunoNome",
                compartilhamento.getAluno().getNome()
        );

        if (compartilhamento.getAluno().getComum() != null) {

            dados.put(
                    "comumOrigemId",
                    compartilhamento.getAluno().getComum().getId()
            );

            dados.put(
                    "comumOrigemNome",
                    compartilhamento.getAluno().getComum().getNome()
            );
        }

        dados.put(
                "comumDestinoId",
                compartilhamento.getComumDestino().getId()
        );

        dados.put(
                "comumDestinoNome",
                compartilhamento.getComumDestino().getNome()
        );

        dados.put(
                "compartilhadoPorUsuarioId",
                compartilhamento.getCompartilhadoPorUsuario().getId()
        );

        dados.put(
                "compartilhadoPorUsuarioNome",
                compartilhamento.getCompartilhadoPorUsuario().getNome()
        );

        return dados;
    }

    private AlunoCompartilhamentoResponseDTO converterParaDTO(
            AlunoCompartilhamento compartilhamento) {

        AlunoCompartilhamentoResponseDTO dto =
                new AlunoCompartilhamentoResponseDTO();

        dto.setId(compartilhamento.getId());

        if (compartilhamento.getAluno() != null) {

            dto.setAlunoId(
                    compartilhamento.getAluno().getId()
            );

            dto.setAlunoNome(
                    compartilhamento.getAluno().getNome()
            );

            if (compartilhamento.getAluno().getComum() != null) {

                dto.setComumOrigemId(
                        compartilhamento.getAluno().getComum().getId()
                );

                dto.setComumOrigemNome(
                        compartilhamento.getAluno().getComum().getNome()
                );
            }
        }

        if (compartilhamento.getComumDestino() != null) {

            dto.setComumDestinoId(
                    compartilhamento.getComumDestino().getId()
            );

            dto.setComumDestinoNome(
                    compartilhamento.getComumDestino().getNome()
            );
        }

        if (compartilhamento.getCompartilhadoPorUsuario() != null) {

            dto.setCompartilhadoPorUsuarioId(
                    compartilhamento.getCompartilhadoPorUsuario().getId()
            );

            dto.setCompartilhadoPorUsuarioNome(
                    compartilhamento.getCompartilhadoPorUsuario().getNome()
            );
        }

        dto.setCriadoEm(
                compartilhamento.getCriadoEm()
        );

        dto.setAtualizadoEm(
                compartilhamento.getAtualizadoEm()
        );

        return dto;
    }
}