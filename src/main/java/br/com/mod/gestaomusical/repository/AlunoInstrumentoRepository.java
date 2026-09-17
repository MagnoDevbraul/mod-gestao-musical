package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.AlunoInstrumento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoInstrumentoRepository extends JpaRepository<AlunoInstrumento, Long> {
}