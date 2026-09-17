package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    List<Aluno> findBySituacaoIgnoreCase(String situacao);
}