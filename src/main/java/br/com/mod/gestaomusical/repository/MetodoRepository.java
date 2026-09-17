package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.Metodo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetodoRepository extends JpaRepository<Metodo, Long> {
}