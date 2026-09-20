package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.AlunoResponseDTO;
import br.com.mod.gestaomusical.dto.AtualizarAlunoRequestDTO;
import br.com.mod.gestaomusical.entity.Aluno;
import br.com.mod.gestaomusical.entity.Historico;
import br.com.mod.gestaomusical.entity.Notificacao;
import br.com.mod.gestaomusical.entity.Usuario;
import br.com.mod.gestaomusical.repository.AlunoRepository;
import br.com.mod.gestaomusical.repository.CargoMinisterioRepository;
import br.com.mod.gestaomusical.repository.ComumCongregacaoRepository;
import br.com.mod.gestaomusical.repository.HistoricoRepository;
import br.com.mod.gestaomusical.repository.NivelRepository;
import br.com.mod.gestaomusical.repository.NotificacaoRepository;
import br.com.mod.gestaomusical.security.UsuarioAutenticadoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AtualizacaoAlunoService {

    private final AlunoRepository alunoRepository;
    private final ComumCongregacaoRepository comumRepository;
    private final NivelRepository nivelRepository;
    private final CargoMinisterioRepository cargoMinisterioRepository;
    private final HistoricoRepository historicoRepository;
    private final NotificacaoRepository notificacaoRepository;
    private final AuditoriaService auditoriaService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public AtualizacaoAlunoService(
            AlunoRepository alunoRepository,
            ComumCongregacaoRepository comumRepository,
            NivelRepository nivelRepository,
            CargoMinisterioRepository cargoMinisterioRepository,
            HistoricoRepository historicoRepository,
            NotificacaoRepository notificacaoRepository,
            AuditoriaService auditoriaService,
            UsuarioAutenticadoService usuarioAutenticadoService) {

        this.alunoRepository = alunoRepository;
        this.comumRepository = comumRepository;
        this.nivelRepository = nivelRepository;
        this.cargoMinisterioRepository = cargoMinisterioRepository;
        this.historicoRepository = historicoRepository;
        this.notificacaoRepository = notificacaoRepository;
        this.auditoriaService = auditoriaService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @Transactional
    public AlunoResponseDTO atualizar(
            Long alunoId,
            AtualizarAlunoRequestDTO dto) {

        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado"
                ));

        if ("ARQUIVADO".equalsIgnoreCase(aluno.getSituacao())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Aluno arquivado não pode ser alterado"
            );
        }

        if (dto.getNome() == null
                || dto.getNome().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Nome do aluno é obrigatório"
            );
        }

        if (dto.getComumId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Comum é obrigatória"
            );
        }

        if (dto.getNivelId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Nível é obrigatório"
            );
        }

        if (dto.getCargoMinisterioId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cargo ministerial é obrigatório"
            );
        }

        /*
         * Usuário real que executou a alteração.
         * Obtido da autenticação do Spring Security.
         */
        Usuario usuario =
                usuarioAutenticadoService
                        .obterUsuarioAutenticado();

        Map<String, Object> dadosAnteriores =
                criarSnapshot(aluno);

        aluno.setNome(
                dto.getNome().trim()
        );

        aluno.setComum(
                comumRepository.findById(dto.getComumId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Comum não encontrada"
                        ))
        );

        aluno.setNivel(
                nivelRepository.findById(dto.getNivelId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Nível não encontrado"
                        ))
        );

        aluno.setCargoMinisterio(
                cargoMinisterioRepository
                        .findById(dto.getCargoMinisterioId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Cargo ministerial não encontrado"
                        ))
        );

        aluno.setPossuiInstrumento(
                dto.getPossuiInstrumento() != null
                        ? dto.getPossuiInstrumento()
                        : false
        );

        aluno.setDataBatismo(
                dto.getDataBatismo()
        );

        aluno.setDataInicioGem(
                dto.getDataInicioGem()
        );

        Aluno alunoSalvo =
                alunoRepository.save(aluno);

        Map<String, Object> dadosNovos =
                criarSnapshot(alunoSalvo);

        /*
         * Histórico.
         */
        Historico historico =
                new Historico();

        historico.setAluno(alunoSalvo);
        historico.setUsuario(usuario);
        historico.setTipoEvento(
                "ATUALIZACAO_ALUNO"
        );

        historico.setDescricao(
                "Dados do aluno atualizados no MOD."
        );

        historico.setValorAnterior(
                dadosAnteriores.toString()
        );

        historico.setValorNovo(
                dadosNovos.toString()
        );

        historicoRepository.save(historico);

        /*
         * Notificação.
         */
        Notificacao notificacao =
                new Notificacao();

        notificacao.setUsuario(usuario);
        notificacao.setAluno(alunoSalvo);
        notificacao.setTipoEvento(
                "ATUALIZACAO_ALUNO"
        );

        notificacao.setTitulo(
                "Aluno atualizado"
        );

        notificacao.setMensagem(
                "Os dados do aluno "
                        + alunoSalvo.getNome()
                        + " foram atualizados no MOD."
        );

        notificacao.setLida(false);
        notificacao.setDataLeitura(null);

        notificacaoRepository.save(notificacao);

        /*
         * Auditoria.
         * O usuário também é obtido automaticamente
         * pelo AuditoriaService.
         */
        auditoriaService.registrar(
                "ATUALIZACAO_ALUNO",
                "aluno",
                alunoSalvo.getId(),
                "Dados do aluno atualizados no MOD.",
                dadosAnteriores,
                dadosNovos
        );

        return converterParaDTO(alunoSalvo);
    }

    private Map<String, Object> criarSnapshot(
            Aluno aluno) {

        Map<String, Object> dados =
                new LinkedHashMap<>();

        dados.put(
                "nome",
                aluno.getNome()
        );

        dados.put(
                "comumId",
                aluno.getComum() != null
                        ? aluno.getComum().getId()
                        : null
        );

        dados.put(
                "nivelId",
                aluno.getNivel() != null
                        ? aluno.getNivel().getId()
                        : null
        );

        dados.put(
                "cargoMinisterioId",
                aluno.getCargoMinisterio() != null
                        ? aluno.getCargoMinisterio().getId()
                        : null
        );

        dados.put(
                "possuiInstrumento",
                aluno.getPossuiInstrumento()
        );

        dados.put(
                "dataBatismo",
                aluno.getDataBatismo()
        );

        dados.put(
                "dataInicioGem",
                aluno.getDataInicioGem()
        );

        dados.put(
                "situacao",
                aluno.getSituacao()
        );

        return dados;
    }

    private AlunoResponseDTO converterParaDTO(
            Aluno aluno) {

        AlunoResponseDTO dto =
                new AlunoResponseDTO();

        dto.setId(
                aluno.getId()
        );

        dto.setNome(
                aluno.getNome()
        );

        if (aluno.getComum() != null) {

            dto.setComumId(
                    aluno.getComum().getId()
            );

            dto.setComumNome(
                    aluno.getComum().getNome()
            );
        }

        if (aluno.getNivel() != null) {

            dto.setNivelId(
                    aluno.getNivel().getId()
            );

            dto.setNivelNome(
                    aluno.getNivel().getNome()
            );
        }

        if (aluno.getCargoMinisterio() != null) {

            dto.setCargoMinisterioId(
                    aluno.getCargoMinisterio().getId()
            );

            dto.setCargoMinisterioNome(
                    aluno.getCargoMinisterio().getNome()
            );
        }

        dto.setPossuiInstrumento(
                aluno.getPossuiInstrumento()
        );

        dto.setDataBatismo(
                aluno.getDataBatismo()
        );

        dto.setDataInicioGem(
                aluno.getDataInicioGem()
        );

        dto.setSituacao(
                aluno.getSituacao()
        );

        dto.setCriadoEm(
                aluno.getCriadoEm()
        );

        dto.setAtualizadoEm(
                aluno.getAtualizadoEm()
        );

        return dto;
    }
}