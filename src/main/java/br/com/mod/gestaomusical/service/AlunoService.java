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

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final ComumCongregacaoRepository comumRepository;
    private final NivelRepository nivelRepository;
    private final CargoMinisterioRepository cargoMinisterioRepository;
    private final AuditoriaService auditoriaService;

    public AlunoService(
            AlunoRepository alunoRepository,
            ComumCongregacaoRepository comumRepository,
            NivelRepository nivelRepository,
            CargoMinisterioRepository cargoMinisterioRepository,
            AuditoriaService auditoriaService) {

        this.alunoRepository = alunoRepository;
        this.comumRepository = comumRepository;
        this.nivelRepository = nivelRepository;
        this.cargoMinisterioRepository = cargoMinisterioRepository;
        this.auditoriaService = auditoriaService;
    }

    @Transactional(readOnly = true)
    public List<AlunoResponseDTO> listarTodos() {

        return alunoRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AlunoResponseDTO> listarArquivados() {

        return alunoRepository
                .findBySituacaoIgnoreCase("ARQUIVADO")
                .stream()
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
                                new RuntimeException(
                                        "Comum não encontrada"
                                ))
        );

        aluno.setNivel(
                nivelRepository.findById(dto.getNivelId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Nível não encontrado"
                                ))
        );

        aluno.setCargoMinisterio(
                cargoMinisterioRepository
                        .findById(dto.getCargoMinisterioId())
                        .orElseThrow(() ->
                                new RuntimeException(
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

        aluno.setSituacao("ATIVO");

        aluno.setCriadoEm(
                LocalDateTime.now()
        );

        aluno.setAtualizadoEm(
                LocalDateTime.now()
        );

        Aluno alunoSalvo =
                alunoRepository.save(aluno);

        /*
         * O AuditoriaService identifica automaticamente
         * o usuário autenticado no Spring Security.
         */
        auditoriaService.registrar(
                "CADASTRO_ALUNO",
                "aluno",
                alunoSalvo.getId(),
                "Aluno cadastrado no MOD.",
                null,
                criarSnapshot(alunoSalvo)
        );

        return alunoSalvo;
    }

    private Map<String, Object> criarSnapshot(
            Aluno aluno) {

        Map<String, Object> dados =
                new LinkedHashMap<>();

        dados.put(
                "id",
                aluno.getId()
        );

        dados.put(
                "nome",
                aluno.getNome()
        );

        if (aluno.getComum() != null) {

            dados.put(
                    "comumId",
                    aluno.getComum().getId()
            );

            dados.put(
                    "comumNome",
                    aluno.getComum().getNome()
            );
        }

        if (aluno.getNivel() != null) {

            dados.put(
                    "nivelId",
                    aluno.getNivel().getId()
            );

            dados.put(
                    "nivelNome",
                    aluno.getNivel().getNome()
            );
        }

        if (aluno.getCargoMinisterio() != null) {

            dados.put(
                    "cargoMinisterioId",
                    aluno.getCargoMinisterio().getId()
            );

            dados.put(
                    "cargoMinisterioNome",
                    aluno.getCargoMinisterio().getNome()
            );
        }

        dados.put(
                "possuiInstrumento",
                aluno.getPossuiInstrumento()
        );

        dados.put(
                "dataBatismo",
                aluno.getDataBatismo() != null
                        ? aluno.getDataBatismo().toString()
                        : null
        );

        dados.put(
                "dataInicioGem",
                aluno.getDataInicioGem() != null
                        ? aluno.getDataInicioGem().toString()
                        : null
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