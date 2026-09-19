package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.AlunoSemMovimentacaoResponseDTO;
import br.com.mod.gestaomusical.entity.Aluno;
import br.com.mod.gestaomusical.entity.Notificacao;
import br.com.mod.gestaomusical.entity.Usuario;
import br.com.mod.gestaomusical.repository.AlunoRepository;
import br.com.mod.gestaomusical.repository.NotificacaoRepository;
import br.com.mod.gestaomusical.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AlertaSemMovimentacaoService {

    private static final String TIPO_EVENTO =
            "ALUNO_SEM_MOVIMENTACAO_60_DIAS";

    private static final String PERFIL_DESTINATARIO =
            "Secretaria";

    private final AlunoSemMovimentacaoService alunoSemMovimentacaoService;
    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacaoRepository notificacaoRepository;

    public AlertaSemMovimentacaoService(
            AlunoSemMovimentacaoService alunoSemMovimentacaoService,
            AlunoRepository alunoRepository,
            UsuarioRepository usuarioRepository,
            NotificacaoRepository notificacaoRepository) {

        this.alunoSemMovimentacaoService = alunoSemMovimentacaoService;
        this.alunoRepository = alunoRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacaoRepository = notificacaoRepository;
    }

    @Transactional
    public int gerarAlertas() {

        List<AlunoSemMovimentacaoResponseDTO> alunosSemMovimentacao =
                alunoSemMovimentacaoService.listarAlunosSemMovimentacao();

        List<Usuario> usuariosSecretaria =
                usuarioRepository
                        .findByPerfilUsuarioNomeIgnoreCaseAndPerfilUsuarioAtivoTrueAndAtivoTrue(
                                PERFIL_DESTINATARIO
                        );

        if (usuariosSecretaria.isEmpty()) {
            throw new IllegalStateException(
                    "Nenhum usuário ativo da Secretaria foi encontrado."
            );
        }

        int totalAlertasGerados = 0;

        for (AlunoSemMovimentacaoResponseDTO dto : alunosSemMovimentacao) {

            Aluno aluno = alunoRepository
                    .findById(dto.getAlunoId())
                    .orElseThrow(() ->
                            new IllegalStateException(
                                    "Aluno não encontrado: " + dto.getAlunoId()
                            )
                    );

            for (Usuario usuario : usuariosSecretaria) {

                if (deveGerarAlerta(dto, usuario)) {

                    Notificacao notificacao = new Notificacao();

                    notificacao.setUsuario(usuario);
                    notificacao.setAluno(aluno);
                    notificacao.setTipoEvento(TIPO_EVENTO);
                    notificacao.setTitulo(
                            "Aluno sem movimentação há 60 dias"
                    );
                    notificacao.setMensagem(
                            "O aluno " +
                                    dto.getAlunoNome() +
                                    " está há " +
                                    dto.getDiasSemMovimentacao() +
                                    " dias sem movimentação musical."
                    );
                    notificacao.setLida(false);

                    notificacaoRepository.save(notificacao);

                    totalAlertasGerados++;
                }
            }
        }

        return totalAlertasGerados;
    }

    private boolean deveGerarAlerta(
            AlunoSemMovimentacaoResponseDTO aluno,
            Usuario usuario) {

        Optional<Notificacao> ultimaNotificacao =
                notificacaoRepository
                        .findTopByAlunoIdAndUsuarioIdAndTipoEventoOrderByDataHoraDesc(
                                aluno.getAlunoId(),
                                usuario.getId(),
                                TIPO_EVENTO
                        );

        if (ultimaNotificacao.isEmpty()) {
            return true;
        }

        Notificacao notificacao = ultimaNotificacao.get();

        if (notificacao.getDataHora() == null) {
            return false;
        }

        /*
         * A dataBase representa o início do período atual
         * sem movimentação.
         *
         * Se ela for posterior ao último alerta, significa
         * que houve uma nova movimentação/início de período
         * depois daquele alerta. Nesse caso, ao completar
         * novamente 60 dias, um novo alerta pode ser criado.
         */
        return notificacao
                .getDataHora()
                .toLocalDate()
                .isBefore(aluno.getDataBase());
    }
}