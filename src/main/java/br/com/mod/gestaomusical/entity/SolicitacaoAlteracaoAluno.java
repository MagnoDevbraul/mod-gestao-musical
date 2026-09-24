package br.com.mod.gestaomusical.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitacao_alteracao_aluno")
public class SolicitacaoAlteracaoAluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "solicitante_id", nullable = false)
    private Usuario solicitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aprovador_id")
    private Usuario aprovador;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusSolicitacaoAlteracaoAluno status;

    @Column(name = "comum_id")
    private Long comumId;

    @Column(name = "nivel_id")
    private Long nivelId;

    @Column(name = "cargo_ministerio_id")
    private Long cargoMinisterioId;

    @Column(name = "data_batismo")
    private LocalDate dataBatismo;

    @Column(name = "data_inicio_gem")
    private LocalDate dataInicioGem;

    @Column(name = "motivo", nullable = false, length = 500)
    private String motivo;

    @Column(name = "observacao_decisao", length = 500)
    private String observacaoDecisao;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "decidido_em")
    private LocalDateTime decididoEm;

    public SolicitacaoAlteracaoAluno() {
    }

    @PrePersist
    public void prePersist() {

        if (criadoEm == null) {
            criadoEm = LocalDateTime.now();
        }

        if (status == null) {
            status = StatusSolicitacaoAlteracaoAluno.PENDENTE;
        }
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

    public Usuario getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Usuario solicitante) {
        this.solicitante = solicitante;
    }

    public Usuario getAprovador() {
        return aprovador;
    }

    public void setAprovador(Usuario aprovador) {
        this.aprovador = aprovador;
    }

    public StatusSolicitacaoAlteracaoAluno getStatus() {
        return status;
    }

    public void setStatus(StatusSolicitacaoAlteracaoAluno status) {
        this.status = status;
    }

    public Long getComumId() {
        return comumId;
    }

    public void setComumId(Long comumId) {
        this.comumId = comumId;
    }

    public Long getNivelId() {
        return nivelId;
    }

    public void setNivelId(Long nivelId) {
        this.nivelId = nivelId;
    }

    public Long getCargoMinisterioId() {
        return cargoMinisterioId;
    }

    public void setCargoMinisterioId(Long cargoMinisterioId) {
        this.cargoMinisterioId = cargoMinisterioId;
    }

    public LocalDate getDataBatismo() {
        return dataBatismo;
    }

    public void setDataBatismo(LocalDate dataBatismo) {
        this.dataBatismo = dataBatismo;
    }

    public LocalDate getDataInicioGem() {
        return dataInicioGem;
    }

    public void setDataInicioGem(LocalDate dataInicioGem) {
        this.dataInicioGem = dataInicioGem;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getObservacaoDecisao() {
        return observacaoDecisao;
    }

    public void setObservacaoDecisao(String observacaoDecisao) {
        this.observacaoDecisao = observacaoDecisao;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getDecididoEm() {
        return decididoEm;
    }

    public void setDecididoEm(LocalDateTime decididoEm) {
        this.decididoEm = decididoEm;
    }
}