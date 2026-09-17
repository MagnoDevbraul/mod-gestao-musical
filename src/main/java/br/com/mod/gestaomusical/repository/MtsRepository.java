package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.Mts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MtsRepository extends JpaRepository<Mts, Long> {
}