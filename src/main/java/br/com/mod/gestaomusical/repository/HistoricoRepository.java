package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.Historico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface HistoricoRepository
        extends JpaRepository<Historico, Long> {

    List<Historico>
    findByAluno_IdAndTipoEventoInOrderByDataHoraDesc(
            Long alunoId,
            Collection<String> tiposEvento
    );

    List<Historico>
    findTop5ByOrderByDataHoraDesc();
}