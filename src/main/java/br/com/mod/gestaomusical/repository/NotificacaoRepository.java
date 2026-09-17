package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
}