package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.AlunoResponseDTO;
import br.com.mod.gestaomusical.entity.Aluno;
import br.com.mod.gestaomusical.entity.Historico;
import br.com.mod.gestaomusical.entity.Notificacao;
import br.com.mod.gestaomusical.entity.Usuario;
import br.com.mod.gestaomusical.repository.AlunoRepository;
import br.com.mod.gestaomusical.repository.HistoricoRepository;
import br.com.mod.gestaomusical.repository.NotificacaoRepository;
import br.com.mod.gestaomusical.security.UsuarioAutenticadoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class RestauracaoAlunoService {

    private final AlunoRepository alunoRepository;
    private final HistoricoRepository historicoRepository;
    private final NotificacaoRepository notificacaoRepository;
    private final AuditoriaService auditoriaService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public RestauracaoAlunoService(
            AlunoRepository alunoRepository,
            HistoricoRepository historicoRepository,
            NotificacaoRepository notificacaoRepository,
            AuditoriaService auditoriaService,
            UsuarioAutenticadoService usuarioAutenticadoService) {

        this.alunoRepository = alunoRepository;
        this.historicoRepository = historicoRepository;
        this.notificacaoRepository = notificacaoRepository;
        this.auditoriaService = auditoriaService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @Transactional
    public AlunoResponseDTO restaurar(Long alunoId) {

        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado"
                ));

        if ("ATIVO".equalsIgnoreCase(aluno.getSituacao())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Aluno já está ativo"
            );
        }

        /*
         * Usuário real que executou a restauração.
         * Obtido diretamente da autenticação.
         */
        Usuario usuario =
                usuarioAutenticadoService.obterUsuarioAutenticado();

        String situacaoAnterior =
                aluno.getSituacao();

        /*
         * 1. Restaura o aluno.
         */
        aluno.setSituacao("ATIVO");

        Aluno alunoSalvo =
                alunoRepository.save(aluno);

        /*
         * 2. Histórico.
         */
        Historico historico =
                new Historico();

        historico.setAluno(alunoSalvo);
        historico.setUsuario(usuario);

        historico.setTipoEvento(
                "RESTAURACAO_ALUNO"
        );

        historico.setDescricao(
                "Aluno restaurado e reativado no MOD."
        );

        historico.setValorAnterior(
                situacaoAnterior
        );

        historico.setValorNovo(
                "ATIVO"
        );

        historicoRepository.save(historico);

        /*
         * 3. Notificação.
         */
        Notificacao notificacao =
                new Notificacao();

        notificacao.setUsuario(usuario);
        notificacao.setAluno(alunoSalvo);

        notificacao.setTipoEvento(
                "RESTAURACAO_ALUNO"
        );

        notificacao.setTitulo(
                "Aluno restaurado"
        );

        notificacao.setMensagem(
                "O aluno "
                        + alunoSalvo.getNome()
                        + " foi restaurado no MOD."
        );

        notificacao.setLida(false);
        notificacao.setDataLeitura(null);

        notificacaoRepository.save(notificacao);

        /*
         * 4. Auditoria.
         * O AuditoriaService também obtém
         * automaticamente o usuário autenticado.
         */
        auditoriaService.registrar(
                "RESTAURACAO_ALUNO",
                "aluno",
                alunoSalvo.getId(),
                "Aluno restaurado da lixeira e reativado no MOD.",
                Map.of(
                        "nome",
                        alunoSalvo.getNome(),
                        "situacao",
                        situacaoAnterior
                ),
                Map.of(
                        "nome",
                        alunoSalvo.getNome(),
                        "situacao",
                        "ATIVO"
                )
        );

        return converterParaDTO(alunoSalvo);
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