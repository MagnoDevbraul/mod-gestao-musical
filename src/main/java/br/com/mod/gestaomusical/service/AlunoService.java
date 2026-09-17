package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.AlunoRequestDTO;
import br.com.mod.gestaomusical.dto.AlunoResponseDTO;
import br.com.mod.gestaomusical.entity.Aluno;
import br.com.mod.gestaomusical.repository.AlunoRepository;
import br.com.mod.gestaomusical.repository.CargoMinisterioRepository;
import br.com.mod.gestaomusical.repository.ComumCongregacaoRepository;
import br.com.mod.gestaomusical.repository.NivelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final ComumCongregacaoRepository comumRepository;
    private final NivelRepository nivelRepository;
    private final CargoMinisterioRepository cargoMinisterioRepository;

    public AlunoService(
            AlunoRepository alunoRepository,
            ComumCongregacaoRepository comumRepository,
            NivelRepository nivelRepository,
            CargoMinisterioRepository cargoMinisterioRepository) {

        this.alunoRepository = alunoRepository;
        this.comumRepository = comumRepository;
        this.nivelRepository = nivelRepository;
        this.cargoMinisterioRepository = cargoMinisterioRepository;
    }

    @Transactional(readOnly = true)
    public List<AlunoResponseDTO> listarTodos() {
        return alunoRepository.findAll().stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AlunoResponseDTO> listarArquivados() {
        return alunoRepository.findBySituacaoIgnoreCase("ARQUIVADO").stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<AlunoResponseDTO> buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .map(this::converterParaDTO);
    }

    @Transactional
    public Aluno salvar(AlunoRequestDTO dto) {

        Aluno aluno = new Aluno();

        aluno.setNome(dto.getNome());

        aluno.setComum(
                comumRepository.findById(dto.getComumId())
                        .orElseThrow(() ->
                                new RuntimeException("Comum não encontrada"))
        );

        aluno.setNivel(
                nivelRepository.findById(dto.getNivelId())
                        .orElseThrow(() ->
                                new RuntimeException("Nível não encontrado"))
        );

        aluno.setCargoMinisterio(
                cargoMinisterioRepository.findById(dto.getCargoMinisterioId())
                        .orElseThrow(() ->
                                new RuntimeException("Cargo ministerial não encontrado"))
        );

        aluno.setPossuiInstrumento(
                dto.getPossuiInstrumento() != null
                        ? dto.getPossuiInstrumento()
                        : false
        );

        aluno.setDataBatismo(dto.getDataBatismo());
        aluno.setDataInicioGem(dto.getDataInicioGem());

        aluno.setSituacao("ATIVO");

        aluno.setCriadoEm(java.time.LocalDateTime.now());
        aluno.setAtualizadoEm(java.time.LocalDateTime.now());

        return alunoRepository.save(aluno);
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