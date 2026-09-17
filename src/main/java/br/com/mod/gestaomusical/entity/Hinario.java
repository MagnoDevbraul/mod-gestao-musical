package br.com.mod.gestaomusical.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "hinario")
public class Hinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "hino", nullable = false)
    private Integer hino;

    @Column(name = "voz", nullable = false)
    private String voz;

    @Column(name = "clave")
    private String clave;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autorizado_por_usuario_id")
    private Usuario autorizadoPorUsuario;

    @Column(name = "observacoes")
    private String observacoes;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    public Hinario() {
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

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Integer getHino() {
        return hino;
    }

    public void setHino(Integer hino) {
        this.hino = hino;
    }

    public String getVoz() {
        return voz;
    }

    public void setVoz(String voz) {
        this.voz = voz;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Usuario getAutorizadoPorUsuario() {
        return autorizadoPorUsuario;
    }

    public void setAutorizadoPorUsuario(Usuario autorizadoPorUsuario) {
        this.autorizadoPorUsuario = autorizadoPorUsuario;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
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