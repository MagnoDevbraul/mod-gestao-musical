package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.Msa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MsaRepository extends JpaRepository<Msa, Long> {
}