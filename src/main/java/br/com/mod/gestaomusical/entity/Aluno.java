package br.com.mod.gestaomusical.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "aluno")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comum_id", nullable = false)
    private ComumCongregacao comum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nivel_id", nullable = false)
    private Nivel nivel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_ministerio_id", nullable = false)
    private CargoMinisterio cargoMinisterio;

    @Column(name = "possui_instrumento", nullable = false)
    private Boolean possuiInstrumento = false;

    @Column(name = "data_batismo")
    private LocalDate dataBatismo;

    @Column(name = "data_inicio_gem")
    private LocalDate dataInicioGem;

    @Column(name = "situacao", nullable = false, length = 30)
    private String situacao = "ATIVO";

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

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

    public Aluno() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ComumCongregacao getComum() {
        return comum;
    }

    public void setComum(ComumCongregacao comum) {
        this.comum = comum;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public CargoMinisterio getCargoMinisterio() {
        return cargoMinisterio;
    }

    public void setCargoMinisterio(CargoMinisterio cargoMinisterio) {
        this.cargoMinisterio = cargoMinisterio;
    }

    public Boolean getPossuiInstrumento() {
        return possuiInstrumento;
    }

    public void setPossuiInstrumento(Boolean possuiInstrumento) {
        this.possuiInstrumento = possuiInstrumento;
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

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
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

}