package br.com.mod.gestaomusical.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "aluno_compartilhamento")
public class AlunoCompartilhamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comum_destino_id", nullable = false)
    private ComumCongregacao comumDestino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compartilhado_por_usuario_id", nullable = false)
    private Usuario compartilhadoPorUsuario;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    public AlunoCompartilhamento() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public ComumCongregacao getComumDestino() {
        return comumDestino;
    }

    public void setComumDestino(ComumCongregacao comumDestino) {
        this.comumDestino = comumDestino;
    }

    public Usuario getCompartilhadoPorUsuario() {
        return compartilhadoPorUsuario;
    }

    public void setCompartilhadoPorUsuario(Usuario compartilhadoPorUsuario) {
        this.compartilhadoPorUsuario = compartilhadoPorUsuario;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    @PrePersist
    protected void aoCriar() {
        LocalDateTime agora = LocalDateTime.now();
        this.criadoEm = agora;
        this.atualizadoEm = agora;
    }

    @PreUpdate
    protected void aoAtualizar() {
        this.atualizadoEm = LocalDateTime.now();
    }
}