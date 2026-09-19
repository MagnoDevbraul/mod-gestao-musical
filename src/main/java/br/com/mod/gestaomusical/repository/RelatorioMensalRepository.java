package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.RelatorioMensal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface RelatorioMensalRepository
        extends JpaRepository<RelatorioMensal, Long> {

    Optional<RelatorioMensal> findByPeriodoInicioAndPeriodoFim(
            LocalDate periodoInicio,
            LocalDate periodoFim
    );

    boolean existsByPeriodoInicioAndPeriodoFim(
            LocalDate periodoInicio,
            LocalDate periodoFim
    );
}