package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.AlunoResponseDTO;
import br.com.mod.gestaomusical.dto.RestaurarAlunoRequestDTO;
import br.com.mod.gestaomusical.entity.Aluno;
import br.com.mod.gestaomusical.entity.Auditoria;
import br.com.mod.gestaomusical.entity.Historico;
import br.com.mod.gestaomusical.entity.Notificacao;
import br.com.mod.gestaomusical.entity.Usuario;
import br.com.mod.gestaomusical.repository.AlunoRepository;
import br.com.mod.gestaomusical.repository.AuditoriaRepository;
import br.com.mod.gestaomusical.repository.HistoricoRepository;
import br.com.mod.gestaomusical.repository.NotificacaoRepository;
import br.com.mod.gestaomusical.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class RestauracaoAlunoService {

    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;
    private final HistoricoRepository historicoRepository;
    private final NotificacaoRepository notificacaoRepository;
    private final AuditoriaRepository auditoriaRepository;

    public RestauracaoAlunoService(
            AlunoRepository alunoRepository,
            UsuarioRepository usuarioRepository,
            HistoricoRepository historicoRepository,
            NotificacaoRepository notificacaoRepository,
            AuditoriaRepository auditoriaRepository) {

        this.alunoRepository = alunoRepository;
        this.usuarioRepository = usuarioRepository;
        this.historicoRepository = historicoRepository;
        this.notificacaoRepository = notificacaoRepository;
        this.auditoriaRepository = auditoriaRepository;
    }

    @Transactional
    public AlunoResponseDTO restaurar(
            Long alunoId,
            RestaurarAlunoRequestDTO dto) {

        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado"
                ));

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado"
                ));

        if ("ATIVO".equalsIgnoreCase(aluno.getSituacao())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Aluno já está ativo"
            );
        }

        String situacaoAnterior = aluno.getSituacao();

        // 1. Restaura o aluno
        aluno.setSituacao("ATIVO");
        alunoRepository.save(aluno);

        // 2. Registra no histórico
        Historico historico = new Historico();
        historico.setAluno(aluno);
        historico.setUsuario(usuario);
        historico.setTipoEvento("RESTAURACAO_ALUNO");
        historico.setDescricao(
                "Aluno restaurado e reativado no MOD."
        );
        historico.setValorAnterior(situacaoAnterior);
        historico.setValorNovo("ATIVO");

        historicoRepository.save(historico);

        // 3. Gera notificação
        Notificacao notificacao = new Notificacao();
        notificacao.setUsuario(usuario);
        notificacao.setAluno(aluno);
        notificacao.setTipoEvento("RESTAURACAO_ALUNO");
        notificacao.setTitulo("Aluno restaurado");
        notificacao.setMensagem(
                "O aluno " + aluno.getNome() + " foi restaurado no MOD."
        );
        notificacao.setLida(false);
        notificacao.setDataLeitura(null);

        notificacaoRepository.save(notificacao);

        // 4. Registra auditoria
        Auditoria auditoria = new Auditoria();
        auditoria.setUsuario(usuario);
        auditoria.setAcao("RESTAURACAO_ALUNO");
        auditoria.setTabelaAfetada("aluno");
        auditoria.setRegistroId(aluno.getId());
        auditoria.setDescricao(
                "Aluno restaurado da lixeira e reativado no MOD."
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
                        "situacao", "ATIVO"
                )
        );

        auditoriaRepository.save(auditoria);

        return converterParaDTO(aluno);
    }

    private AlunoResponseDTO converterParaDTO(Aluno aluno) {

        AlunoResponseDTO dto = new AlunoResponseDTO();

        dto.setId(aluno.getId());
        dto.setNome(aluno.getNome());

        if (aluno.getComum() != null) {
            dto.setComumId(aluno.getComum().getId());
            dto.setComumNome(aluno.getComum().getNome());
        }

        if (aluno.getNivel() != null) {
            dto.setNivelId(aluno.getNivel().getId());
            dto.setNivelNome(aluno.getNivel().getNome());
        }

        if (aluno.getCargoMinisterio() != null) {
            dto.setCargoMinisterioId(aluno.getCargoMinisterio().getId());
            dto.setCargoMinisterioNome(
                    aluno.getCargoMinisterio().getNome()
            );
        }

        dto.setPossuiInstrumento(aluno.getPossuiInstrumento());
        dto.setDataBatismo(aluno.getDataBatismo());
        dto.setDataInicioGem(aluno.getDataInicioGem());
        dto.setSituacao(aluno.getSituacao());
        dto.setCriadoEm(aluno.getCriadoEm());
        dto.setAtualizadoEm(aluno.getAtualizadoEm());

        return dto;
    }
}