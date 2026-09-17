package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
}