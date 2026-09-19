package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.AlunoSemMovimentacaoResponseDTO;
import br.com.mod.gestaomusical.repository.AlunoSemMovimentacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class AlunoSemMovimentacaoService {

    private static final int DIAS_SEM_MOVIMENTACAO = 60;

    private final AlunoSemMovimentacaoRepository repository;

    public AlunoSemMovimentacaoService(
            AlunoSemMovimentacaoRepository repository) {

        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<AlunoSemMovimentacaoResponseDTO> listarAlunosSemMovimentacao() {

        return repository
                .buscarAlunosSemMovimentacao(DIAS_SEM_MOVIMENTACAO)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    private AlunoSemMovimentacaoResponseDTO converterParaDTO(Object[] resultado) {

        Long alunoId = ((Number) resultado[0]).longValue();
        String alunoNome = (String) resultado[1];

        LocalDate ultimaMovimentacao = converterParaLocalDate(resultado[2]);
        LocalDate dataInicioGem = converterParaLocalDate(resultado[3]);
        LocalDate dataBase = converterParaLocalDate(resultado[4]);

        Long diasSemMovimentacao =
                ((Number) resultado[5]).longValue();

        return new AlunoSemMovimentacaoResponseDTO(
                alunoId,
                alunoNome,
                ultimaMovimentacao,
                dataInicioGem,
                dataBase,
                diasSemMovimentacao
        );
    }

    private LocalDate converterParaLocalDate(Object valor) {

        if (valor == null) {
            return null;
        }

        if (valor instanceof LocalDate localDate) {
            return localDate;
        }

        if (valor instanceof Date sqlDate) {
            return sqlDate.toLocalDate();
        }

        throw new IllegalArgumentException(
                "Tipo de data não suportado: " + valor.getClass().getName()
        );
    }
}