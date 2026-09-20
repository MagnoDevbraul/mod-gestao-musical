package br.com.mod.gestaomusical.dto;

import java.time.LocalDate;

public class MtsRequestDTO {

    private Long alunoId;

    private LocalDate data;
    private Integer modulo;
    private Integer licao;
    private Integer paginaInicial;
    private Integer paginaFinal;
    private String observacoes;

    public MtsRequestDTO() {
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Integer getModulo() {
        return modulo;
    }

    public void setModulo(Integer modulo) {
        this.modulo = modulo;
    }

    public Integer getLicao() {
        return licao;
    }

    public void setLicao(Integer licao) {
        this.licao = licao;
    }

    public Integer getPaginaInicial() {
        return paginaInicial;
    }

    public void setPaginaInicial(Integer paginaInicial) {
        this.paginaInicial = paginaInicial;
    }

    public Integer getPaginaFinal() {
        return paginaFinal;
    }

    public void setPaginaFinal(Integer paginaFinal) {
        this.paginaFinal = paginaFinal;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}