package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.Instrumento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstrumentoRepository extends JpaRepository<Instrumento, Long> {
}