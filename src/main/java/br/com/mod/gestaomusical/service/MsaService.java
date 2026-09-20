package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.MsaRequestDTO;
import br.com.mod.gestaomusical.dto.MsaResponseDTO;
import br.com.mod.gestaomusical.entity.Aluno;
import br.com.mod.gestaomusical.entity.Historico;
import br.com.mod.gestaomusical.entity.Msa;
import br.com.mod.gestaomusical.entity.Notificacao;
import br.com.mod.gestaomusical.entity.Usuario;
import br.com.mod.gestaomusical.repository.AlunoRepository;
import br.com.mod.gestaomusical.repository.HistoricoRepository;
import br.com.mod.gestaomusical.repository.MsaRepository;
import br.com.mod.gestaomusical.repository.NotificacaoRepository;
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
public class MsaService {

    private static final Set<String> CLAVES_VALIDAS =
            Set.of("SOL", "DÓ", "FÁ");

    private final MsaRepository msaRepository;
    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;
    private final HistoricoRepository historicoRepository;
    private final NotificacaoRepository notificacaoRepository;
    private final AuditoriaService auditoriaService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public MsaService(
            MsaRepository msaRepository,
            AlunoRepository alunoRepository,
            UsuarioRepository usuarioRepository,
            HistoricoRepository historicoRepository,
            NotificacaoRepository notificacaoRepository,
            AuditoriaService auditoriaService,
            UsuarioAutenticadoService usuarioAutenticadoService) {

        this.msaRepository = msaRepository;
        this.alunoRepository = alunoRepository;
        this.usuarioRepository = usuarioRepository;
        this.historicoRepository = historicoRepository;
        this.notificacaoRepository = notificacaoRepository;
        this.auditoriaService = auditoriaService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @Transactional(readOnly = true)
    public List<MsaResponseDTO> listarTodos() {

        return msaRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<MsaResponseDTO> buscarPorId(Long id) {

        return msaRepository.findById(id)
                .map(this::converterParaDTO);
    }

    @Transactional
    public MsaResponseDTO salvar(MsaRequestDTO dto) {

        validar(dto);

        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado"
                ));

        if ("ARQUIVADO".equalsIgnoreCase(aluno.getSituacao())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível registrar MSA para aluno arquivado"
            );
        }

        /*
         * Usuário que realmente executou a operação.
         * Obtido da autenticação do Spring Security.
         */
        Usuario usuario =
                usuarioAutenticadoService.obterUsuarioAutenticado();

        /*
         * Usuário que autorizou o progresso musical.
         * Continua sendo informado no registro de MSA.
         */
        Usuario autorizadoPor = usuarioRepository
                .findById(dto.getAutorizadoPorUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário informado em 'Autorizado por' não encontrado"
                ));

        Msa msa = new Msa();

        msa.setAluno(aluno);
        msa.setData(dto.getData());
        msa.setFase(dto.getFase().trim());
        msa.setPaginaInicial(dto.getPaginaInicial());
        msa.setPaginaFinal(dto.getPaginaFinal());
        msa.setLicaoInicial(dto.getLicaoInicial());
        msa.setLicaoFinal(dto.getLicaoFinal());
        msa.setClave(dto.getClave().trim().toUpperCase());
        msa.setAutorizadoPorUsuario(autorizadoPor);
        msa.setObservacoes(dto.getObservacoes());

        Msa msaSalvo = msaRepository.save(msa);

        criarHistorico(
                msaSalvo,
                aluno,
                usuario,
                autorizadoPor
        );

        criarNotificacao(
                msaSalvo,
                aluno,
                usuario,
                autorizadoPor
        );

        auditoriaService.registrar(
                "REGISTRO_MSA",
                "msa",
                msaSalvo.getId(),
                "Registro de progresso em MSA criado no MOD.",
                null,
                criarSnapshot(msaSalvo)
        );

        return converterParaDTO(msaSalvo);
    }

    private void validar(MsaRequestDTO dto) {

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

        validarFase(dto.getFase());
        validarClave(dto.getClave());
        validarPaginas(
                dto.getPaginaInicial(),
                dto.getPaginaFinal()
        );
        validarLicoes(
                dto.getLicaoInicial(),
                dto.getLicaoFinal()
        );
    }

    private void validarFase(String fase) {

        if (fase == null || fase.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Fase é obrigatória"
            );
        }

        String valor = fase.trim();

        if (!valor.matches("^([1-9]|1[0-6])\\.[1-3]$")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Fase inválida. Informe uma fase entre 1.1 e 16.3"
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

    private void validarPaginas(
            Integer paginaInicial,
            Integer paginaFinal) {

        if (paginaInicial == null && paginaFinal == null) {
            return;
        }

        if (paginaInicial == null || paginaFinal == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Página inicial e página final devem ser informadas juntas"
            );
        }

        if (paginaInicial < 1 || paginaFinal < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "As páginas devem ser maiores ou iguais a 1"
            );
        }

        if (paginaFinal < paginaInicial) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Página final não pode ser menor que a página inicial"
            );
        }
    }

    private void validarLicoes(
            Integer licaoInicial,
            Integer licaoFinal) {

        if (licaoInicial == null && licaoFinal == null) {
            return;
        }

        if (licaoInicial == null || licaoFinal == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Lição inicial e lição final devem ser informadas juntas"
            );
        }

        if (licaoInicial < 1 || licaoFinal < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "As lições devem ser maiores ou iguais a 1"
            );
        }

        if (licaoFinal < licaoInicial) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Lição final não pode ser menor que a lição inicial"
            );
        }
    }

    private void criarHistorico(
            Msa msa,
            Aluno aluno,
            Usuario usuario,
            Usuario autorizadoPor) {

        Historico historico = new Historico();

        historico.setAluno(aluno);
        historico.setUsuario(usuario);
        historico.setTipoEvento("REGISTRO_MSA");
        historico.setDescricao(
                "Progresso de MSA registrado no MOD."
        );
        historico.setValorAnterior(null);
        historico.setValorNovo(
                criarDescricaoProgresso(
                        msa,
                        autorizadoPor
                )
        );

        historicoRepository.save(historico);
    }

    private void criarNotificacao(
            Msa msa,
            Aluno aluno,
            Usuario usuario,
            Usuario autorizadoPor) {

        Notificacao notificacao = new Notificacao();

        notificacao.setUsuario(usuario);
        notificacao.setAluno(aluno);
        notificacao.setTipoEvento("REGISTRO_MSA");
        notificacao.setTitulo("MSA registrado");

        notificacao.setMensagem(
                "Foi registrado progresso de MSA para o aluno "
                        + aluno.getNome()
                        + ": "
                        + criarDescricaoProgresso(
                        msa,
                        autorizadoPor
                )
                        + "."
        );

        notificacao.setLida(false);
        notificacao.setDataLeitura(null);

        notificacaoRepository.save(notificacao);
    }

    private String criarDescricaoProgresso(
            Msa msa,
            Usuario autorizadoPor) {

        StringBuilder descricao =
                new StringBuilder();

        descricao.append("Fase ")
                .append(msa.getFase());

        if (msa.getPaginaInicial() != null) {
            descricao.append(" - Páginas ")
                    .append(msa.getPaginaInicial())
                    .append(" a ")
                    .append(msa.getPaginaFinal());
        }

        if (msa.getLicaoInicial() != null) {
            descricao.append(" - Lições ")
                    .append(msa.getLicaoInicial())
                    .append(" a ")
                    .append(msa.getLicaoFinal());
        }

        descricao.append(" - Clave ")
                .append(msa.getClave());

        descricao.append(" - Autorizado por ")
                .append(autorizadoPor.getNome());

        return descricao.toString();
    }

    private Map<String, Object> criarSnapshot(Msa msa) {

        Map<String, Object> dados =
                new LinkedHashMap<>();

        dados.put(
                "alunoId",
                msa.getAluno().getId()
        );

        dados.put(
                "data",
                msa.getData().toString()
        );

        dados.put(
                "fase",
                msa.getFase()
        );

        dados.put(
                "paginaInicial",
                msa.getPaginaInicial()
        );

        dados.put(
                "paginaFinal",
                msa.getPaginaFinal()
        );

        dados.put(
                "licaoInicial",
                msa.getLicaoInicial()
        );

        dados.put(
                "licaoFinal",
                msa.getLicaoFinal()
        );

        dados.put(
                "clave",
                msa.getClave()
        );

        if (msa.getAutorizadoPorUsuario() != null) {

            dados.put(
                    "autorizadoPorUsuarioId",
                    msa.getAutorizadoPorUsuario().getId()
            );

            dados.put(
                    "autorizadoPorUsuarioNome",
                    msa.getAutorizadoPorUsuario().getNome()
            );
        }

        dados.put(
                "observacoes",
                msa.getObservacoes()
        );

        return dados;
    }

    private MsaResponseDTO converterParaDTO(Msa msa) {

        MsaResponseDTO dto =
                new MsaResponseDTO();

        dto.setId(msa.getId());

        if (msa.getAluno() != null) {
            dto.setAlunoId(
                    msa.getAluno().getId()
            );

            dto.setAlunoNome(
                    msa.getAluno().getNome()
            );
        }

        dto.setData(
                msa.getData()
        );

        dto.setFase(
                msa.getFase()
        );

        dto.setPaginaInicial(
                msa.getPaginaInicial()
        );

        dto.setPaginaFinal(
                msa.getPaginaFinal()
        );

        dto.setLicaoInicial(
                msa.getLicaoInicial()
        );

        dto.setLicaoFinal(
                msa.getLicaoFinal()
        );

        dto.setClave(
                msa.getClave()
        );

        if (msa.getAutorizadoPorUsuario() != null) {

            dto.setAutorizadoPorUsuarioId(
                    msa.getAutorizadoPorUsuario().getId()
            );

            dto.setAutorizadoPorUsuarioNome(
                    msa.getAutorizadoPorUsuario().getNome()
            );
        }

        dto.setObservacoes(
                msa.getObservacoes()
        );

        dto.setCriadoEm(
                msa.getCriadoEm()
        );

        dto.setAtualizadoEm(
                msa.getAtualizadoEm()
        );

        return dto;
    }
}