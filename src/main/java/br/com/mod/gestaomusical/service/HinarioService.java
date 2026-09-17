package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.HinarioRequestDTO;
import br.com.mod.gestaomusical.dto.HinarioResponseDTO;
import br.com.mod.gestaomusical.entity.Aluno;
import br.com.mod.gestaomusical.entity.Auditoria;
import br.com.mod.gestaomusical.entity.Hinario;
import br.com.mod.gestaomusical.entity.Historico;
import br.com.mod.gestaomusical.entity.Notificacao;
import br.com.mod.gestaomusical.entity.Usuario;
import br.com.mod.gestaomusical.repository.AlunoRepository;
import br.com.mod.gestaomusical.repository.AuditoriaRepository;
import br.com.mod.gestaomusical.repository.HinarioRepository;
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
import java.util.Set;

@Service
public class HinarioService {

    private static final Set<String> VOZES_VALIDAS =
            Set.of("PRINCIPAL", "ALTERNATIVA");

    private static final Set<String> CLAVES_VALIDAS =
            Set.of("SOL", "DÓ", "FÁ");

    private final HinarioRepository hinarioRepository;
    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;
    private final HistoricoRepository historicoRepository;
    private final NotificacaoRepository notificacaoRepository;
    private final AuditoriaRepository auditoriaRepository;

    public HinarioService(
            HinarioRepository hinarioRepository,
            AlunoRepository alunoRepository,
            UsuarioRepository usuarioRepository,
            HistoricoRepository historicoRepository,
            NotificacaoRepository notificacaoRepository,
            AuditoriaRepository auditoriaRepository) {

        this.hinarioRepository = hinarioRepository;
        this.alunoRepository = alunoRepository;
        this.usuarioRepository = usuarioRepository;
        this.historicoRepository = historicoRepository;
        this.notificacaoRepository = notificacaoRepository;
        this.auditoriaRepository = auditoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<HinarioResponseDTO> listarTodos() {
        return hinarioRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<HinarioResponseDTO> buscarPorId(Long id) {
        return hinarioRepository.findById(id)
                .map(this::converterParaDTO);
    }

    @Transactional
    public HinarioResponseDTO salvar(HinarioRequestDTO dto) {

        validar(dto);

        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado"
                ));

        if ("ARQUIVADO".equalsIgnoreCase(aluno.getSituacao())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível registrar Hinário para aluno arquivado"
            );
        }

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário responsável pelo lançamento não encontrado"
                ));

        Usuario autorizadoPor = usuarioRepository
                .findById(dto.getAutorizadoPorUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário informado em 'Autorizado por' não encontrado"
                ));

        Hinario hinario = new Hinario();

        hinario.setAluno(aluno);
        hinario.setData(dto.getData());
        hinario.setHino(dto.getHino());
        hinario.setVoz(dto.getVoz().trim().toUpperCase());
        hinario.setClave(dto.getClave().trim().toUpperCase());
        hinario.setAutorizadoPorUsuario(autorizadoPor);
        hinario.setObservacoes(dto.getObservacoes());

        Hinario hinarioSalvo = hinarioRepository.save(hinario);

        criarHistorico(
                hinarioSalvo,
                aluno,
                usuario,
                autorizadoPor
        );

        criarNotificacao(
                hinarioSalvo,
                aluno,
                usuario,
                autorizadoPor
        );

        criarAuditoria(
                hinarioSalvo,
                usuario
        );

        return converterParaDTO(hinarioSalvo);
    }

    private void validar(HinarioRequestDTO dto) {

        if (dto.getAlunoId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Aluno é obrigatório"
            );
        }

        if (dto.getUsuarioId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Usuário responsável pelo lançamento é obrigatório"
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

        if (dto.getHino() == null || dto.getHino() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Hino deve ser maior que 0"
            );
        }

        validarVoz(dto.getVoz());
        validarClave(dto.getClave());
    }

    private void validarVoz(String voz) {

        if (voz == null || voz.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Voz é obrigatória"
            );
        }

        String valor = voz.trim().toUpperCase();

        if (!VOZES_VALIDAS.contains(valor)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Voz inválida. Valores permitidos: PRINCIPAL ou ALTERNATIVA"
            );
        }
    }

    private void validarClave(String clave) {

        if (clave == null || clave.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Clave é obrigatória"
            );
        }

        String valor = clave.trim().toUpperCase();

        if (!CLAVES_VALIDAS.contains(valor)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Clave inválida. Valores permitidos: SOL, DÓ ou FÁ"
            );
        }
    }

    private void criarHistorico(
            Hinario hinario,
            Aluno aluno,
            Usuario usuario,
            Usuario autorizadoPor) {

        Historico historico = new Historico();

        historico.setAluno(aluno);
        historico.setUsuario(usuario);
        historico.setTipoEvento("REGISTRO_HINARIO");
        historico.setDescricao(
                "Progresso de Hinário registrado no MOD."
        );
        historico.setValorAnterior(null);
        historico.setValorNovo(
                criarDescricaoProgresso(
                        hinario,
                        autorizadoPor
                )
        );

        historicoRepository.save(historico);
    }

    private void criarNotificacao(
            Hinario hinario,
            Aluno aluno,
            Usuario usuario,
            Usuario autorizadoPor) {

        Notificacao notificacao = new Notificacao();

        notificacao.setUsuario(usuario);
        notificacao.setAluno(aluno);
        notificacao.setTipoEvento("REGISTRO_HINARIO");
        notificacao.setTitulo("Hinário registrado");

        notificacao.setMensagem(
                "Foi registrado progresso de Hinário para o aluno "
                        + aluno.getNome()
                        + ": "
                        + criarDescricaoProgresso(
                        hinario,
                        autorizadoPor
                )
                        + "."
        );

        notificacao.setLida(false);
        notificacao.setDataLeitura(null);

        notificacaoRepository.save(notificacao);
    }

    private void criarAuditoria(
            Hinario hinario,
            Usuario usuario) {

        Auditoria auditoria = new Auditoria();

        auditoria.setUsuario(usuario);
        auditoria.setAcao("REGISTRO_HINARIO");
        auditoria.setTabelaAfetada("hinario");
        auditoria.setRegistroId(hinario.getId());
        auditoria.setDescricao(
                "Registro de progresso em Hinário criado no MOD."
        );

        auditoria.setDadosAnteriores(null);
        auditoria.setDadosNovos(
                criarSnapshot(hinario)
        );

        auditoriaRepository.save(auditoria);
    }

    private String criarDescricaoProgresso(
            Hinario hinario,
            Usuario autorizadoPor) {

        return "Hino "
                + hinario.getHino()
                + " - Voz "
                + hinario.getVoz()
                + " - Clave "
                + hinario.getClave()
                + " - Autorizado por "
                + autorizadoPor.getNome();
    }

    private Map<String, Object> criarSnapshot(
            Hinario hinario) {

        Map<String, Object> dados =
                new LinkedHashMap<>();

        dados.put(
                "alunoId",
                hinario.getAluno().getId()
        );

        dados.put(
                "data",
                hinario.getData().toString()
        );

        dados.put(
                "hino",
                hinario.getHino()
        );

        dados.put(
                "voz",
                hinario.getVoz()
        );

        dados.put(
                "clave",
                hinario.getClave()
        );

        if (hinario.getAutorizadoPorUsuario() != null) {

            dados.put(
                    "autorizadoPorUsuarioId",
                    hinario.getAutorizadoPorUsuario().getId()
            );

            dados.put(
                    "autorizadoPorUsuarioNome",
                    hinario.getAutorizadoPorUsuario().getNome()
            );
        }

        dados.put(
                "observacoes",
                hinario.getObservacoes()
        );

        return dados;
    }

    private HinarioResponseDTO converterParaDTO(
            Hinario hinario) {

        HinarioResponseDTO dto =
                new HinarioResponseDTO();

        dto.setId(hinario.getId());

        if (hinario.getAluno() != null) {
            dto.setAlunoId(
                    hinario.getAluno().getId()
            );

            dto.setAlunoNome(
                    hinario.getAluno().getNome()
            );
        }

        dto.setData(
                hinario.getData()
        );

        dto.setHino(
                hinario.getHino()
        );

        dto.setVoz(
                hinario.getVoz()
        );

        dto.setClave(
                hinario.getClave()
        );

        if (hinario.getAutorizadoPorUsuario() != null) {

            dto.setAutorizadoPorUsuarioId(
                    hinario.getAutorizadoPorUsuario().getId()
            );

            dto.setAutorizadoPorUsuarioNome(
                    hinario.getAutorizadoPorUsuario().getNome()
            );
        }

        dto.setObservacoes(
                hinario.getObservacoes()
        );

        dto.setCriadoEm(
                hinario.getCriadoEm()
        );

        dto.setAtualizadoEm(
                hinario.getAtualizadoEm()
        );

        return dto;
    }
}