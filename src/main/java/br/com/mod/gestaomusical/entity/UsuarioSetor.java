package br.com.mod.gestaomusical.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "usuario_setor",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_usuario_setor",
                        columnNames = {
                                "usuario_id",
                                "setor_id"
                        }
                )
        }
)
public class UsuarioSetor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Representa o usuário vinculado ao setor.
     *
     * O relacionamento é mantido nesta entidade intermediária
     * porque um usuário pode possuir vínculos registrados
     * diretamente na tabela usuario_setor.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "setor_id", nullable = false)
    private Setor setor;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    public UsuarioSetor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}