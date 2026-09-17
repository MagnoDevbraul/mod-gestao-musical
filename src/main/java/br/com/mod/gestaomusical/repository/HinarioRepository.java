package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.Hinario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HinarioRepository extends JpaRepository<Hinario, Long> {
}