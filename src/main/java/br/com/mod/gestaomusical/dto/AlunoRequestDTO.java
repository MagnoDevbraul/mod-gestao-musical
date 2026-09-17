package br.com.mod.gestaomusical.dto;

import java.time.LocalDate;

public class AlunoRequestDTO {

    private String nome;

    private Long comumId;
    private Long nivelId;
    private Long cargoMinisterioId;

    private Boolean possuiInstrumento;

    private LocalDate dataBatismo;
    private LocalDate dataInicioGem;

    public AlunoRequestDTO() {
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
}