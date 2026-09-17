package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.NotificacaoRequestDTO;
import br.com.mod.gestaomusical.dto.NotificacaoResponseDTO;
import br.com.mod.gestaomusical.entity.Aluno;
import br.com.mod.gestaomusical.entity.Notificacao;
import br.com.mod.gestaomusical.entity.Usuario;
import br.com.mod.gestaomusical.repository.AlunoRepository;
import br.com.mod.gestaomusical.repository.NotificacaoRepository;
import br.com.mod.gestaomusical.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

@Service
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlunoRepository alunoRepository;

    public NotificacaoService(
            NotificacaoRepository notificacaoRepository,
            UsuarioRepository usuarioRepository,
            AlunoRepository alunoRepository) {

        this.notificacaoRepository = notificacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.alunoRepository = alunoRepository;
    }

    @Transactional(readOnly = true)
    public List<NotificacaoResponseDTO> listarTodos() {
        return notificacaoRepository.findAll().stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<NotificacaoResponseDTO> buscarPorId(Long id) {
        return notificacaoRepository.findById(id)
                .map(this::converterParaDTO);
    }

    @Transactional
    public NotificacaoResponseDTO salvar(NotificacaoRequestDTO dto) {

        if (dto.getTipoEvento() == null || dto.getTipoEvento().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tipo do evento é obrigatório"
            );
        }

        if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Título da notificação é obrigatório"
            );
        }

        if (dto.getMensagem() == null || dto.getMensagem().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Mensagem da notificação é obrigatória"
            );
        }

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado"
                ));

        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado"
                ));

        Notificacao notificacao = new Notificacao();

        notificacao.setUsuario(usuario);
        notificacao.setAluno(aluno);
        notificacao.setTipoEvento(dto.getTipoEvento());
        notificacao.setTitulo(dto.getTitulo());
        notificacao.setMensagem(dto.getMensagem());
        notificacao.setLida(false);
        notificacao.setDataLeitura(null);

        Notificacao salva = notificacaoRepository.save(notificacao);

        return converterParaDTO(salva);
    }

    @Transactional
    public NotificacaoResponseDTO marcarComoLida(Long id) {

        Notificacao notificacao = notificacaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Notificação não encontrada"
                ));

        if (!Boolean.TRUE.equals(notificacao.getLida())) {
            notificacao.setLida(true);
            notificacao.setDataLeitura(LocalDateTime.now());

            notificacao = notificacaoRepository.save(notificacao);
        }

        return converterParaDTO(notificacao);
    }

    private NotificacaoResponseDTO converterParaDTO(Notificacao notificacao) {

        NotificacaoResponseDTO dto = new NotificacaoResponseDTO();

        dto.setId(notificacao.getId());

        if (notificacao.getUsuario() != null) {
            dto.setUsuarioId(notificacao.getUsuario().getId());
            dto.setUsuarioNome(notificacao.getUsuario().getNome());
        }

        if (notificacao.getAluno() != null) {
            dto.setAlunoId(notificacao.getAluno().getId());
            dto.setAlunoNome(notificacao.getAluno().getNome());
        }

        dto.setTipoEvento(notificacao.getTipoEvento());
        dto.setTitulo(notificacao.getTitulo());
        dto.setMensagem(notificacao.getMensagem());
        dto.setDataHora(notificacao.getDataHora());
        dto.setLida(notificacao.getLida());
        dto.setDataLeitura(notificacao.getDataLeitura());

        return dto;
    }
}