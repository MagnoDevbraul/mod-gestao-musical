package br.com.mod.gestaomusical.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "exclusao_aluno")
public class ExclusaoAluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "motivo", nullable = false)
    private String motivo;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    public ExclusaoAluno() {
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    @PrePersist
    protected void aoCriar() {
        if (this.dataHora == null) {
            this.dataHora = LocalDateTime.now();
        }
    }
}