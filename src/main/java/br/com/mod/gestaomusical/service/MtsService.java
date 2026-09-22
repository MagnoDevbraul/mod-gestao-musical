package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.MtsRequestDTO;
import br.com.mod.gestaomusical.dto.MtsResponseDTO;
import br.com.mod.gestaomusical.entity.Aluno;
import br.com.mod.gestaomusical.entity.Historico;
import br.com.mod.gestaomusical.entity.Mts;
import br.com.mod.gestaomusical.entity.Notificacao;
import br.com.mod.gestaomusical.entity.Usuario;
import br.com.mod.gestaomusical.repository.AlunoRepository;
import br.com.mod.gestaomusical.repository.HistoricoRepository;
import br.com.mod.gestaomusical.repository.MtsRepository;
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

@Service
public class MtsService {

    private final MtsRepository mtsRepository;
    private final AlunoRepository alunoRepository;
    private final HistoricoRepository historicoRepository;
    private final NotificacaoRepository notificacaoRepository;
    private final AuditoriaService auditoriaService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;
    private final AutorizadorMusicalService autorizadorMusicalService;

    public MtsService(
            MtsRepository mtsRepository,
            AlunoRepository alunoRepository,
            HistoricoRepository historicoRepository,
            NotificacaoRepository notificacaoRepository,
            AuditoriaService auditoriaService,
            UsuarioAutenticadoService usuarioAutenticadoService,
            AutorizadorMusicalService autorizadorMusicalService) {

        this.mtsRepository = mtsRepository;
        this.alunoRepository = alunoRepository;
        this.historicoRepository = historicoRepository;
        this.notificacaoRepository = notificacaoRepository;
        this.auditoriaService = auditoriaService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
        this.autorizadorMusicalService = autorizadorMusicalService;
    }

    @Transactional(readOnly = true)
    public List<MtsResponseDTO> listarTodos() {

        return mtsRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<MtsResponseDTO> buscarPorId(Long id) {

        return mtsRepository.findById(id)
                .map(this::converterParaDTO);
    }

    @Transactional
    public MtsResponseDTO salvar(MtsRequestDTO dto) {

        validar(dto);

        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado"
                ));

        if ("ARQUIVADO".equalsIgnoreCase(aluno.getSituacao())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível registrar MTS para aluno arquivado"
            );
        }

        /*
         * Autor da operação.
         * Sempre corresponde ao usuário realmente autenticado.
         */
        Usuario usuario =
                usuarioAutenticadoService.obterUsuarioAutenticado();

        /*
         * Resolve o autorizador musical utilizando a mesma regra
         * aplicada aos demais módulos de progresso musical.
         */
        Usuario autorizadoPor =
                autorizadorMusicalService.resolverAutorizador(
                        usuario,
                        dto.getAutorizadoPorUsuarioId()
                );

        Mts mts = new Mts();

        mts.setAluno(aluno);
        mts.setData(dto.getData());
        mts.setModulo(dto.getModulo());
        mts.setLicao(dto.getLicao());
        mts.setPaginaInicial(dto.getPaginaInicial());
        mts.setPaginaFinal(dto.getPaginaFinal());
        mts.setAutorizadoPorUsuario(autorizadoPor);
        mts.setObservacoes(dto.getObservacoes());

        Mts mtsSalvo =
                mtsRepository.save(mts);

        criarHistorico(
                mtsSalvo,
                aluno,
                usuario,
                autorizadoPor
        );

        criarNotificacao(
                mtsSalvo,
                aluno,
                usuario,
                autorizadoPor
        );

        auditoriaService.registrar(
                "REGISTRO_MTS",
                "mts",
                mtsSalvo.getId(),
                "Registro de progresso em MTS criado no MOD.",
                null,
                criarSnapshot(mtsSalvo)
        );

        return converterParaDTO(mtsSalvo);
    }

    private void validar(MtsRequestDTO dto) {

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

        if (dto.getModulo() == null
                || dto.getModulo() < 1
                || dto.getModulo() > 12) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Módulo deve estar entre 1 e 12"
            );
        }

        if (dto.getLicao() == null
                || dto.getLicao() < 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Lição deve ser maior ou igual a 0"
            );
        }

        if (dto.getPaginaInicial() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Página inicial é obrigatória"
            );
        }

        if (dto.getPaginaFinal() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Página final é obrigatória"
            );
        }

        if (dto.getPaginaInicial() < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Página inicial deve ser maior ou igual a 1"
            );
        }

        if (dto.getPaginaFinal() < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Página final deve ser maior ou igual a 1"
            );
        }

        if (dto.getPaginaFinal()
                < dto.getPaginaInicial()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Página final não pode ser menor que a página inicial"
            );
        }
    }

    private void criarHistorico(
            Mts mts,
            Aluno aluno,
            Usuario usuario,
            Usuario autorizadoPor) {

        Historico historico =
                new Historico();

        historico.setAluno(aluno);

        /*
         * O histórico registra quem executou a operação.
         * O autorizador musical permanece registrado separadamente
         * no próprio MTS e na descrição do progresso.
         */
        historico.setUsuario(usuario);

        historico.setTipoEvento(
                "REGISTRO_MTS"
        );

        historico.setDescricao(
                "Progresso de MTS registrado no MOD."
        );

        historico.setValorAnterior(null);

        historico.setValorNovo(
                criarDescricaoProgresso(
                        mts,
                        autorizadoPor
                )
        );

        historicoRepository.save(historico);
    }

    private void criarNotificacao(
            Mts mts,
            Aluno aluno,
            Usuario usuario,
            Usuario autorizadoPor) {

        Notificacao notificacao =
                new Notificacao();

        notificacao.setUsuario(usuario);
        notificacao.setAluno(aluno);
        notificacao.setTipoEvento("REGISTRO_MTS");
        notificacao.setTitulo("MTS registrado");

        notificacao.setMensagem(
                "Foi registrado progresso de MTS para o aluno "
                        + aluno.getNome()
                        + ": "
                        + criarDescricaoProgresso(
                        mts,
                        autorizadoPor
                )
                        + "."
        );

        notificacao.setLida(false);
        notificacao.setDataLeitura(null);

        notificacaoRepository.save(notificacao);
    }

    private String criarDescricaoProgresso(
            Mts mts,
            Usuario autorizadoPor) {

        return "Módulo "
                + mts.getModulo()
                + " - Lição "
                + mts.getLicao()
                + " - Páginas "
                + mts.getPaginaInicial()
                + " a "
                + mts.getPaginaFinal()
                + " - Autorizado por "
                + autorizadoPor.getNome();
    }

    private Map<String, Object> criarSnapshot(
            Mts mts) {

        Map<String, Object> dados =
                new LinkedHashMap<>();

        dados.put(
                "alunoId",
                mts.getAluno().getId()
        );

        dados.put(
                "data",
                mts.getData().toString()
        );

        dados.put(
                "modulo",
                mts.getModulo()
        );

        dados.put(
                "licao",
                mts.getLicao()
        );

        dados.put(
                "paginaInicial",
                mts.getPaginaInicial()
        );

        dados.put(
                "paginaFinal",
                mts.getPaginaFinal()
        );

        if (mts.getAutorizadoPorUsuario() != null) {

            dados.put(
                    "autorizadoPorUsuarioId",
                    mts.getAutorizadoPorUsuario().getId()
            );

            dados.put(
                    "autorizadoPorUsuarioNome",
                    mts.getAutorizadoPorUsuario().getNome()
            );
        }

        dados.put(
                "observacoes",
                mts.getObservacoes()
        );

        return dados;
    }

    private MtsResponseDTO converterParaDTO(
            Mts mts) {

        MtsResponseDTO dto =
                new MtsResponseDTO();

        dto.setId(mts.getId());

        if (mts.getAluno() != null) {

            dto.setAlunoId(
                    mts.getAluno().getId()
            );

            dto.setAlunoNome(
                    mts.getAluno().getNome()
            );
        }

        if (mts.getAutorizadoPorUsuario() != null) {

            dto.setAutorizadoPorUsuarioId(
                    mts.getAutorizadoPorUsuario().getId()
            );

            dto.setAutorizadoPorUsuarioNome(
                    mts.getAutorizadoPorUsuario().getNome()
            );
        }

        dto.setData(
                mts.getData()
        );

        dto.setModulo(
                mts.getModulo()
        );

        dto.setLicao(
                mts.getLicao()
        );

        dto.setPaginaInicial(
                mts.getPaginaInicial()
        );

        dto.setPaginaFinal(
                mts.getPaginaFinal()
        );

        dto.setObservacoes(
                mts.getObservacoes()
        );

        dto.setCriadoEm(
                mts.getCriadoEm()
        );

        dto.setAtualizadoEm(
                mts.getAtualizadoEm()
        );

        return dto;
    }
}