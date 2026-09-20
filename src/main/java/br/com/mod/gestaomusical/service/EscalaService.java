package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.EscalaRequestDTO;
import br.com.mod.gestaomusical.dto.EscalaResponseDTO;
import br.com.mod.gestaomusical.entity.Aluno;
import br.com.mod.gestaomusical.entity.Escala;
import br.com.mod.gestaomusical.entity.Historico;
import br.com.mod.gestaomusical.entity.Notificacao;
import br.com.mod.gestaomusical.entity.Tonalidade;
import br.com.mod.gestaomusical.entity.Usuario;
import br.com.mod.gestaomusical.repository.AlunoRepository;
import br.com.mod.gestaomusical.repository.EscalaRepository;
import br.com.mod.gestaomusical.repository.HistoricoRepository;
import br.com.mod.gestaomusical.repository.NotificacaoRepository;
import br.com.mod.gestaomusical.repository.TonalidadeRepository;
import br.com.mod.gestaomusical.repository.UsuarioRepository;
import br.com.mod.gestaomusical.security.UsuarioAutenticadoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class EscalaService {

    private static final Set<String> CLAVES_VALIDAS =
            Set.of("SOL", "DÓ", "FÁ");

    private final EscalaRepository escalaRepository;
    private final AlunoRepository alunoRepository;
    private final TonalidadeRepository tonalidadeRepository;
    private final UsuarioRepository usuarioRepository;
    private final HistoricoRepository historicoRepository;
    private final NotificacaoRepository notificacaoRepository;
    private final AuditoriaService auditoriaService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public EscalaService(
            EscalaRepository escalaRepository,
            AlunoRepository alunoRepository,
            TonalidadeRepository tonalidadeRepository,
            UsuarioRepository usuarioRepository,
            HistoricoRepository historicoRepository,
            NotificacaoRepository notificacaoRepository,
            AuditoriaService auditoriaService,
            UsuarioAutenticadoService usuarioAutenticadoService) {

        this.escalaRepository = escalaRepository;
        this.alunoRepository = alunoRepository;
        this.tonalidadeRepository = tonalidadeRepository;
        this.usuarioRepository = usuarioRepository;
        this.historicoRepository = historicoRepository;
        this.notificacaoRepository = notificacaoRepository;
        this.auditoriaService = auditoriaService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @Transactional(readOnly = true)
    public List<EscalaResponseDTO> listarTodos() {

        return escalaRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<EscalaResponseDTO> buscarPorId(Long id) {

        return escalaRepository.findById(id)
                .map(this::converterParaDTO);
    }

    @Transactional
    public EscalaResponseDTO salvar(EscalaRequestDTO dto) {

        validar(dto);

        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado"
                ));

        if ("ARQUIVADO".equalsIgnoreCase(aluno.getSituacao())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível registrar Escala para aluno arquivado"
            );
        }

        Tonalidade tonalidade = tonalidadeRepository
                .findById(dto.getTonalidadeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tonalidade não encontrada"
                ));

        /*
         * Usuário que realmente executou o lançamento.
         * Obtido da autenticação do Spring Security.
         */
        Usuario usuario =
                usuarioAutenticadoService.obterUsuarioAutenticado();

        /*
         * Usuário responsável pela autorização musical.
         */
        Usuario autorizadoPor = usuarioRepository
                .findById(dto.getAutorizadoPorUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário informado em 'Autorizado por' não encontrado"
                ));

        Escala escala = new Escala();

        escala.setAluno(aluno);
        escala.setData(dto.getData());
        escala.setNomeEscala(dto.getNomeEscala().trim());
        escala.setTonalidade(tonalidade);
        escala.setClave(dto.getClave().trim().toUpperCase());
        escala.setAutorizadoPorUsuario(autorizadoPor);
        escala.setObservacoes(dto.getObservacoes());

        Escala escalaSalva =
                escalaRepository.save(escala);

        criarHistorico(
                escalaSalva,
                aluno,
                usuario,
                autorizadoPor
        );

        criarNotificacao(
                escalaSalva,
                aluno,
                usuario,
                autorizadoPor
        );

        auditoriaService.registrar(
                "REGISTRO_ESCALA",
                "escala",
                escalaSalva.getId(),
                "Registro de progresso em Escala criado no MOD.",
                null,
                criarSnapshot(escalaSalva)
        );

        return converterParaDTO(escalaSalva);
    }

    private void validar(EscalaRequestDTO dto) {

        if (dto.getAlunoId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Aluno é obrigatório"
            );
        }

        if (dto.getAutorizadoPorUsuarioId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Autorizado por é obrigatório"
            );
        }

        if (dto.getData() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Data é obrigatória"
            );
        }

        if (dto.getNomeEscala() == null
                || dto.getNomeEscala().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Nome da escala é obrigatório"
            );
        }

        if (dto.getTonalidadeId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tonalidade é obrigatória"
            );
        }

        validarClave(dto.getClave());
    }

    private void validarClave(String clave) {

        if (clave == null || clave.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Clave é obrigatória"
            );
        }

        String valor =
                clave.trim().toUpperCase();

        if (!CLAVES_VALIDAS.contains(valor)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Clave inválida. Valores permitidos: SOL, DÓ ou FÁ"
            );
        }
    }

    private void criarHistorico(
            Escala escala,
            Aluno aluno,
            Usuario usuario,
            Usuario autorizadoPor) {

        Historico historico =
                new Historico();

        historico.setAluno(aluno);
        historico.setUsuario(usuario);
        historico.setTipoEvento("REGISTRO_ESCALA");

        historico.setDescricao(
                "Progresso de Escala registrado no MOD."
        );

        historico.setValorAnterior(null);

        historico.setValorNovo(
                criarDescricaoProgresso(
                        escala,
                        autorizadoPor
                )
        );

        historicoRepository.save(historico);
    }

    private void criarNotificacao(
            Escala escala,
            Aluno aluno,
            Usuario usuario,
            Usuario autorizadoPor) {

        Notificacao notificacao =
                new Notificacao();

        notificacao.setUsuario(usuario);
        notificacao.setAluno(aluno);
        notificacao.setTipoEvento("REGISTRO_ESCALA");
        notificacao.setTitulo("Escala registrada");

        notificacao.setMensagem(
                "Foi registrado progresso de Escala para o aluno "
                        + aluno.getNome()
                        + ": "
                        + criarDescricaoProgresso(
                        escala,
                        autorizadoPor
                )
                        + "."
        );

        notificacao.setLida(false);
        notificacao.setDataLeitura(null);

        notificacaoRepository.save(notificacao);
    }

    private String criarDescricaoProgresso(
            Escala escala,
            Usuario autorizadoPor) {

        return "Escala "
                + escala.getNomeEscala()
                + " - Tonalidade "
                + escala.getTonalidade().getNome()
                + " - Clave "
                + escala.getClave()
                + " - Autorizado por "
                + autorizadoPor.getNome();
    }

    private Map<String, Object> criarSnapshot(
            Escala escala) {

        Map<String, Object> dados =
                new LinkedHashMap<>();

        dados.put(
                "alunoId",
                escala.getAluno().getId()
        );

        dados.put(
                "data",
                escala.getData().toString()
        );

        dados.put(
                "nomeEscala",
                escala.getNomeEscala()
        );

        dados.put(
                "tonalidadeId",
                escala.getTonalidade().getId()
        );

        dados.put(
                "tonalidadeNome",
                escala.getTonalidade().getNome()
        );

        dados.put(
                "clave",
                escala.getClave()
        );

        if (escala.getAutorizadoPorUsuario() != null) {

            dados.put(
                    "autorizadoPorUsuarioId",
                    escala.getAutorizadoPorUsuario().getId()
            );

            dados.put(
                    "autorizadoPorUsuarioNome",
                    escala.getAutorizadoPorUsuario().getNome()
            );
        }

        dados.put(
                "observacoes",
                escala.getObservacoes()
        );

        return dados;
    }

    private EscalaResponseDTO converterParaDTO(
            Escala escala) {

        EscalaResponseDTO dto =
                new EscalaResponseDTO();

        dto.setId(escala.getId());

        if (escala.getAluno() != null) {

            dto.setAlunoId(
                    escala.getAluno().getId()
            );

            dto.setAlunoNome(
                    escala.getAluno().getNome()
            );
        }

        dto.setData(
                escala.getData()
        );

        dto.setNomeEscala(
                escala.getNomeEscala()
        );

        if (escala.getTonalidade() != null) {

            dto.setTonalidadeId(
                    escala.getTonalidade().getId()
            );

            dto.setTonalidadeNome(
                    escala.getTonalidade().getNome()
            );
        }

        dto.setClave(
                escala.getClave()
        );

        if (escala.getAutorizadoPorUsuario() != null) {

            dto.setAutorizadoPorUsuarioId(
                    escala.getAutorizadoPorUsuario().getId()
            );

            dto.setAutorizadoPorUsuarioNome(
                    escala.getAutorizadoPorUsuario().getNome()
            );
        }

        dto.setObservacoes(
                escala.getObservacoes()
        );

        dto.setCriadoEm(
                escala.getCriadoEm()
        );

        dto.setAtualizadoEm(
                escala.getAtualizadoEm()
        );

        return dto;
    }
}