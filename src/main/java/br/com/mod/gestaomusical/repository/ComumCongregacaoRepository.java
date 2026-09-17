package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.ComumCongregacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComumCongregacaoRepository
        extends JpaRepository<ComumCongregacao, Long> {
}