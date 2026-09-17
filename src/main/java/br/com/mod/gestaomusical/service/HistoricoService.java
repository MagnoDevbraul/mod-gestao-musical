package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.HistoricoRequestDTO;
import br.com.mod.gestaomusical.dto.HistoricoResponseDTO;
import br.com.mod.gestaomusical.entity.Aluno;
import br.com.mod.gestaomusical.entity.Historico;
import br.com.mod.gestaomusical.entity.Usuario;
import br.com.mod.gestaomusical.repository.AlunoRepository;
import br.com.mod.gestaomusical.repository.HistoricoRepository;
import br.com.mod.gestaomusical.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistoricoService {

    private final HistoricoRepository historicoRepository;
    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;

    public HistoricoService(
            HistoricoRepository historicoRepository,
            AlunoRepository alunoRepository,
            UsuarioRepository usuarioRepository) {

        this.historicoRepository = historicoRepository;
        this.alunoRepository = alunoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<HistoricoResponseDTO> listarTodos() {
        return historicoRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public Optional<HistoricoResponseDTO> buscarPorId(Long id) {
        return historicoRepository.findById(id)
                .map(this::converterParaDTO);
    }

    public HistoricoResponseDTO salvar(HistoricoRequestDTO dto) {

        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Historico historico = new Historico();

        historico.setAluno(aluno);
        historico.setUsuario(usuario);
        historico.setTipoEvento(dto.getTipoEvento());
        historico.setDescricao(dto.getDescricao());
        historico.setValorAnterior(dto.getValorAnterior());
        historico.setValorNovo(dto.getValorNovo());

        Historico salvo = historicoRepository.save(historico);

        return converterParaDTO(salvo);
    }

    private HistoricoResponseDTO converterParaDTO(Historico historico) {

        HistoricoResponseDTO dto = new HistoricoResponseDTO();

        dto.setId(historico.getId());

        if (historico.getAluno() != null) {
            dto.setAlunoId(historico.getAluno().getId());
            dto.setAlunoNome(historico.getAluno().getNome());
        }

        if (historico.getUsuario() != null) {
            dto.setUsuarioId(historico.getUsuario().getId());
            dto.setUsuarioNome(historico.getUsuario().getNome());
        }

        dto.setTipoEvento(historico.getTipoEvento());
        dto.setDataHora(historico.getDataHora());
        dto.setDescricao(historico.getDescricao());
        dto.setValorAnterior(historico.getValorAnterior());
        dto.setValorNovo(historico.getValorNovo());

        return dto;
    }
}