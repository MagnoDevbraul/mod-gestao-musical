package br.com.mod.gestaomusical.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SolicitacaoAlteracaoAlunoResponseDTO {

    private Long id;

    private Long alunoId;
    private String alunoNome;

    private Long solicitanteId;
    private String solicitanteNome;

    private Long aprovadorId;
    private String aprovadorNome;

    private String status;

    private Long comumId;
    private Long nivelId;
    private Long cargoMinisterioId;

    private LocalDate dataBatismo;
    private LocalDate dataInicioGem;

    private String motivo;
    private String observacaoDecisao;

    private LocalDateTime criadoEm;
    private LocalDateTime decididoEm;

    public SolicitacaoAlteracaoAlunoResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public String getAlunoNome() {
        return alunoNome;
    }

    public void setAlunoNome(String alunoNome) {
        this.alunoNome = alunoNome;
    }

    public Long getSolicitanteId() {
        return solicitanteId;
    }

    public void setSolicitanteId(Long solicitanteId) {
        this.solicitanteId = solicitanteId;
    }

    public String getSolicitanteNome() {
        return solicitanteNome;
    }

    public void setSolicitanteNome(String solicitanteNome) {
        this.solicitanteNome = solicitanteNome;
    }

    public Long getAprovadorId() {
        return aprovadorId;
    }

    public void setAprovadorId(Long aprovadorId) {
        this.aprovadorId = aprovadorId;
    }

    public String getAprovadorNome() {
        return aprovadorNome;
    }

    public void setAprovadorNome(String aprovadorNome) {
        this.aprovadorNome = aprovadorNome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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