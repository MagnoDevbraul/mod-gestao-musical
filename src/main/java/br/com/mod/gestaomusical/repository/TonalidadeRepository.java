package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.Tonalidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TonalidadeRepository extends JpaRepository<Tonalidade, Long> {
}