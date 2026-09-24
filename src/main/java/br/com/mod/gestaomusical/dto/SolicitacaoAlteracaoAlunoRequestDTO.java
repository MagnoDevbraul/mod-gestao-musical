package br.com.mod.gestaomusical.dto;

import java.time.LocalDate;

public class SolicitacaoAlteracaoAlunoRequestDTO {

    private Long comumId;
    private Long nivelId;
    private Long cargoMinisterioId;

    private LocalDate dataBatismo;
    private LocalDate dataInicioGem;

    private String motivo;

    public SolicitacaoAlteracaoAlunoRequestDTO() {
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
}