package br.com.mod.gestaomusical.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AlunoResponseDTO {

    private Long id;
    private String nome;

    private Long comumId;
    private String comumNome;

    private Long nivelId;
    private String nivelNome;

    private Long cargoMinisterioId;
    private String cargoMinisterioNome;

    private Boolean possuiInstrumento;
    private LocalDate dataBatismo;
    private LocalDate dataInicioGem;
    private String situacao;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public AlunoResponseDTO() {
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

    public Long getComumId() {
        return comumId;
    }

    public void setComumId(Long comumId) {
        this.comumId = comumId;
    }

    public String getComumNome() {
        return comumNome;
    }

    public void setComumNome(String comumNome) {
        this.comumNome = comumNome;
    }

    public Long getNivelId() {
        return nivelId;
    }

    public void setNivelId(Long nivelId) {
        this.nivelId = nivelId;
    }

    public String getNivelNome() {
        return nivelNome;
    }

    public void setNivelNome(String nivelNome) {
        this.nivelNome = nivelNome;
    }

    public Long getCargoMinisterioId() {
        return cargoMinisterioId;
    }

    public void setCargoMinisterioId(Long cargoMinisterioId) {
        this.cargoMinisterioId = cargoMinisterioId;
    }

    public String getCargoMinisterioNome() {
        return cargoMinisterioNome;
    }

    public void setCargoMinisterioNome(String cargoMinisterioNome) {
        this.cargoMinisterioNome = cargoMinisterioNome;
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