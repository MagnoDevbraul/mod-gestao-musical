package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.Historico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoRepository extends JpaRepository<Historico, Long> {
}