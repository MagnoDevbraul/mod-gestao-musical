package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.MetodoRequestDTO;
import br.com.mod.gestaomusical.dto.MetodoResponseDTO;
import br.com.mod.gestaomusical.entity.Aluno;
import br.com.mod.gestaomusical.entity.Historico;
import br.com.mod.gestaomusical.entity.Metodo;
import br.com.mod.gestaomusical.entity.Notificacao;
import br.com.mod.gestaomusical.entity.Usuario;
import br.com.mod.gestaomusical.repository.AlunoRepository;
import br.com.mod.gestaomusical.repository.HistoricoRepository;
import br.com.mod.gestaomusical.repository.MetodoRepository;
import br.com.mod.gestaomusical.repository.NotificacaoRepository;
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
public class MetodoService {

    private static final Set<String> CLAVES_VALIDAS =
            Set.of("SOL", "DÓ", "FÁ");

    private final MetodoRepository metodoRepository;
    private final AlunoRepository alunoRepository;
    private final HistoricoRepository historicoRepository;
    private final NotificacaoRepository notificacaoRepository;
    private final AuditoriaService auditoriaService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;
    private final AutorizadorMusicalService autorizadorMusicalService;

    public MetodoService(
            MetodoRepository metodoRepository,
            AlunoRepository alunoRepository,
            HistoricoRepository historicoRepository,
            NotificacaoRepository notificacaoRepository,
            AuditoriaService auditoriaService,
            UsuarioAutenticadoService usuarioAutenticadoService,
            AutorizadorMusicalService autorizadorMusicalService) {

        this.metodoRepository = metodoRepository;
        this.alunoRepository = alunoRepository;
        this.historicoRepository = historicoRepository;
        this.notificacaoRepository = notificacaoRepository;
        this.auditoriaService = auditoriaService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
        this.autorizadorMusicalService = autorizadorMusicalService;
    }

    @Transactional(readOnly = true)
    public List<MetodoResponseDTO> listarTodos() {

        return metodoRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<MetodoResponseDTO> buscarPorId(Long id) {

        return metodoRepository.findById(id)
                .map(this::converterParaDTO);
    }

    @Transactional
    public MetodoResponseDTO salvar(MetodoRequestDTO dto) {

        validar(dto);

        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado"
                ));

        if ("ARQUIVADO".equalsIgnoreCase(aluno.getSituacao())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível registrar Método para aluno arquivado"
            );
        }

        /*
         * Autor da operação.
         * Sempre corresponde ao usuário realmente autenticado.
         */
        Usuario usuario =
                usuarioAutenticadoService.obterUsuarioAutenticado();

        /*
         * Resolve quem será registrado como autorizador musical.
         *
         * Quando nenhum ID é informado, utiliza o próprio usuário
         * autenticado. A indicação de terceiros é validada pelo
         * AutorizadorMusicalService.
         */
        Usuario autorizadoPor =
                autorizadorMusicalService.resolverAutorizador(
                        usuario,
                        dto.getAutorizadoPorUsuarioId()
                );

        Metodo metodo = new Metodo();

        metodo.setAluno(aluno);
        metodo.setData(dto.getData());
        metodo.setNomeMetodo(dto.getNomeMetodo().trim());

        metodo.setPaginaInicial(dto.getPaginaInicial());
        metodo.setPaginaFinal(dto.getPaginaFinal());

        metodo.setLicaoInicial(dto.getLicaoInicial());
        metodo.setLicaoFinal(dto.getLicaoFinal());

        metodo.setClave(dto.getClave().trim().toUpperCase());
        metodo.setAutorizadoPorUsuario(autorizadoPor);

        metodo.setObservacoes(dto.getObservacoes());

        Metodo metodoSalvo =
                metodoRepository.save(metodo);

        criarHistorico(
                metodoSalvo,
                aluno,
                usuario,
                autorizadoPor
        );

        criarNotificacao(
                metodoSalvo,
                aluno,
                usuario,
                autorizadoPor
        );

        auditoriaService.registrar(
                "REGISTRO_METODO",
                "metodo",
                metodoSalvo.getId(),
                "Registro de progresso em Método criado no MOD.",
                null,
                criarSnapshot(metodoSalvo)
        );

        return converterParaDTO(metodoSalvo);
    }

    private void validar(MetodoRequestDTO dto) {

        if (dto.getAlunoId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Aluno é obrigatório"
            );
        }

        /*
         * autorizadoPorUsuarioId é opcional.
         * Quando ausente, o próprio usuário autenticado
         * será utilizado como autorizador.
         */

        if (dto.getData() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Data é obrigatória"
            );
        }

        if (dto.getNomeMetodo() == null
                || dto.getNomeMetodo().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Nome do método é obrigatório"
            );
        }

        validarPaginas(
                dto.getPaginaInicial(),
                dto.getPaginaFinal()
        );

        validarLicoes(
                dto.getLicaoInicial(),
                dto.getLicaoFinal()
        );

        validarClave(dto.getClave());
    }

    private void validarPaginas(
            Integer paginaInicial,
            Integer paginaFinal) {

        if (paginaInicial == null || paginaFinal == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Página inicial e página final são obrigatórias"
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

        if (licaoInicial == null || licaoFinal == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Lição inicial e lição final são obrigatórias"
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
            Metodo metodo,
            Aluno aluno,
            Usuario usuario,
            Usuario autorizadoPor) {

        Historico historico =
                new Historico();

        historico.setAluno(aluno);

        /*
         * O histórico registra o executor real da operação,
         * independentemente de quem autorizou o progresso musical.
         */
        historico.setUsuario(usuario);

        historico.setTipoEvento("REGISTRO_METODO");

        historico.setDescricao(
                "Progresso de Método registrado no MOD."
        );

        historico.setValorAnterior(null);

        historico.setValorNovo(
                criarDescricaoProgresso(
                        metodo,
                        autorizadoPor
                )
        );

        historicoRepository.save(historico);
    }

    private void criarNotificacao(
            Metodo metodo,
            Aluno aluno,
            Usuario usuario,
            Usuario autorizadoPor) {

        Notificacao notificacao =
                new Notificacao();

        notificacao.setUsuario(usuario);
        notificacao.setAluno(aluno);
        notificacao.setTipoEvento("REGISTRO_METODO");
        notificacao.setTitulo("Método registrado");

        notificacao.setMensagem(
                "Foi registrado progresso de Método para o aluno "
                        + aluno.getNome()
                        + ": "
                        + criarDescricaoProgresso(
                        metodo,
                        autorizadoPor
                )
                        + "."
        );

        notificacao.setLida(false);
        notificacao.setDataLeitura(null);

        notificacaoRepository.save(notificacao);
    }

    private String criarDescricaoProgresso(
            Metodo metodo,
            Usuario autorizadoPor) {

        return "Método "
                + metodo.getNomeMetodo()
                + " - Páginas "
                + metodo.getPaginaInicial()
                + " a "
                + metodo.getPaginaFinal()
                + " - Lições "
                + metodo.getLicaoInicial()
                + " a "
                + metodo.getLicaoFinal()
                + " - Clave "
                + metodo.getClave()
                + " - Autorizado por "
                + autorizadoPor.getNome();
    }

    private Map<String, Object> criarSnapshot(
            Metodo metodo) {

        Map<String, Object> dados =
                new LinkedHashMap<>();

        dados.put(
                "alunoId",
                metodo.getAluno().getId()
        );

        dados.put(
                "data",
                metodo.getData().toString()
        );

        dados.put(
                "nomeMetodo",
                metodo.getNomeMetodo()
        );

        dados.put(
                "paginaInicial",
                metodo.getPaginaInicial()
        );

        dados.put(
                "paginaFinal",
                metodo.getPaginaFinal()
        );

        dados.put(
                "licaoInicial",
                metodo.getLicaoInicial()
        );

        dados.put(
                "licaoFinal",
                metodo.getLicaoFinal()
        );

        dados.put(
                "clave",
                metodo.getClave()
        );

        if (metodo.getAutorizadoPorUsuario() != null) {

            dados.put(
                    "autorizadoPorUsuarioId",
                    metodo.getAutorizadoPorUsuario().getId()
            );

            dados.put(
                    "autorizadoPorUsuarioNome",
                    metodo.getAutorizadoPorUsuario().getNome()
            );
        }

        dados.put(
                "observacoes",
                metodo.getObservacoes()
        );

        return dados;
    }

    private MetodoResponseDTO converterParaDTO(
            Metodo metodo) {

        MetodoResponseDTO dto =
                new MetodoResponseDTO();

        dto.setId(metodo.getId());

        if (metodo.getAluno() != null) {

            dto.setAlunoId(
                    metodo.getAluno().getId()
            );

            dto.setAlunoNome(
                    metodo.getAluno().getNome()
            );
        }

        dto.setData(
                metodo.getData()
        );

        dto.setNomeMetodo(
                metodo.getNomeMetodo()
        );

        dto.setPaginaInicial(
                metodo.getPaginaInicial()
        );

        dto.setPaginaFinal(
                metodo.getPaginaFinal()
        );

        dto.setLicaoInicial(
                metodo.getLicaoInicial()
        );

        dto.setLicaoFinal(
                metodo.getLicaoFinal()
        );

        dto.setClave(
                metodo.getClave()
        );

        if (metodo.getAutorizadoPorUsuario() != null) {

            dto.setAutorizadoPorUsuarioId(
                    metodo.getAutorizadoPorUsuario().getId()
            );

            dto.setAutorizadoPorUsuarioNome(
                    metodo.getAutorizadoPorUsuario().getNome()
            );
        }

        dto.setObservacoes(
                metodo.getObservacoes()
        );

        dto.setCriadoEm(
                metodo.getCriadoEm()
        );

        dto.setAtualizadoEm(
                metodo.getAtualizadoEm()
        );

        return dto;
    }
}