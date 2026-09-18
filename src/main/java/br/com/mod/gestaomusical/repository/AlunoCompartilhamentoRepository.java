package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.AlunoCompartilhamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlunoCompartilhamentoRepository
        extends JpaRepository<AlunoCompartilhamento, Long> {

    Optional<AlunoCompartilhamento> findByAlunoId(Long alunoId);

    boolean existsByAlunoId(Long alunoId);
}