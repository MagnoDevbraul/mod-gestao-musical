package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.ExclusaoAluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExclusaoAlunoRepository extends JpaRepository<ExclusaoAluno, Long> {
}
