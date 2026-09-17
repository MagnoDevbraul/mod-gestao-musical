package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.AlunoInstrumentoRequestDTO;
import br.com.mod.gestaomusical.dto.AlunoInstrumentoResponseDTO;
import br.com.mod.gestaomusical.entity.Aluno;
import br.com.mod.gestaomusical.entity.AlunoInstrumento;
import br.com.mod.gestaomusical.entity.Instrumento;
import br.com.mod.gestaomusical.entity.Tonalidade;
import br.com.mod.gestaomusical.repository.AlunoInstrumentoRepository;
import br.com.mod.gestaomusical.repository.AlunoRepository;
import br.com.mod.gestaomusical.repository.InstrumentoRepository;
import br.com.mod.gestaomusical.repository.TonalidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlunoInstrumentoService {

    private final AlunoInstrumentoRepository repository;
    private final AlunoRepository alunoRepository;
    private final InstrumentoRepository instrumentoRepository;
    private final TonalidadeRepository tonalidadeRepository;

    public AlunoInstrumentoService(
            AlunoInstrumentoRepository repository,
            AlunoRepository alunoRepository,
            InstrumentoRepository instrumentoRepository,
            TonalidadeRepository tonalidadeRepository) {

        this.repository = repository;
        this.alunoRepository = alunoRepository;
        this.instrumentoRepository = instrumentoRepository;
        this.tonalidadeRepository = tonalidadeRepository;
    }

    public List<AlunoInstrumentoResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public Optional<AlunoInstrumentoResponseDTO> buscarPorId(Long id) {
        return repository.findById(id)
                .map(this::converterParaDTO);
    }

    public AlunoInstrumentoResponseDTO salvar(AlunoInstrumentoRequestDTO dto) {

        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Instrumento instrumento = instrumentoRepository.findById(dto.getInstrumentoId())
                .orElseThrow(() -> new RuntimeException("Instrumento não encontrado"));

        Tonalidade tonalidade = tonalidadeRepository.findById(dto.getTonalidadeId())
                .orElseThrow(() -> new RuntimeException("Tonalidade não encontrada"));

        AlunoInstrumento alunoInstrumento = new AlunoInstrumento();

        alunoInstrumento.setAluno(aluno);
        alunoInstrumento.setInstrumento(instrumento);
        alunoInstrumento.setTipo(dto.getTipo());
        alunoInstrumento.setTonalidade(tonalidade);

        AlunoInstrumento salvo = repository.save(alunoInstrumento);

        return converterParaDTO(salvo);
    }

    private AlunoInstrumentoResponseDTO converterParaDTO(AlunoInstrumento entidade) {

        AlunoInstrumentoResponseDTO dto = new AlunoInstrumentoResponseDTO();

        dto.setId(entidade.getId());

        if (entidade.getAluno() != null) {
            dto.setAlunoId(entidade.getAluno().getId());
            dto.setAlunoNome(entidade.getAluno().getNome());
        }

        if (entidade.getInstrumento() != null) {
            dto.setInstrumentoId(entidade.getInstrumento().getId());
            dto.setInstrumentoNome(entidade.getInstrumento().getNome());
        }

        dto.setTipo(entidade.getTipo());

        if (entidade.getTonalidade() != null) {
            dto.setTonalidadeId(entidade.getTonalidade().getId());
            dto.setTonalidadeNome(entidade.getTonalidade().getNome());
        }

        return dto;
    }
}