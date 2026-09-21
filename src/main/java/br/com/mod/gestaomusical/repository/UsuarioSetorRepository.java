package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.UsuarioSetor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioSetorRepository
        extends JpaRepository<UsuarioSetor, Long> {

    /*
     * Retorna todos os setores aos quais determinado usuário
     * está formalmente vinculado.
     *
     * Esse vínculo será utilizado nas regras de autorização,
     * em vez da comum/congregação associada ao usuário.
     */
    List<UsuarioSetor> findByUsuario_Id(Long usuarioId);

    /*
     * Permite verificar diretamente se determinado usuário
     * possui vínculo com um setor específico.
     */
    boolean existsByUsuario_IdAndSetor_Id(
            Long usuarioId,
            Long setorId
    );
}