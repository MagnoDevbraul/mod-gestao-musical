package br.com.mod.gestaomusical.dto;

import java.time.LocalDate;

public class AlunoSemMovimentacaoResponseDTO {

    private Long alunoId;
    private String alunoNome;
    private LocalDate ultimaMovimentacao;
    private LocalDate dataInicioGem;
    private LocalDate dataBase;
    private Long diasSemMovimentacao;

    public AlunoSemMovimentacaoResponseDTO() {
    }

    public AlunoSemMovimentacaoResponseDTO(
            Long alunoId,
            String alunoNome,
            LocalDate ultimaMovimentacao,
            LocalDate dataInicioGem,
            LocalDate dataBase,
            Long diasSemMovimentacao) {

        this.alunoId = alunoId;
        this.alunoNome = alunoNome;
        this.ultimaMovimentacao = ultimaMovimentacao;
        this.dataInicioGem = dataInicioGem;
        this.dataBase = dataBase;
        this.diasSemMovimentacao = diasSemMovimentacao;
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

    public LocalDate getUltimaMovimentacao() {
        return ultimaMovimentacao;
    }

    public void setUltimaMovimentacao(LocalDate ultimaMovimentacao) {
        this.ultimaMovimentacao = ultimaMovimentacao;
    }

    public LocalDate getDataInicioGem() {
        return dataInicioGem;
    }

    public void setDataInicioGem(LocalDate dataInicioGem) {
        this.dataInicioGem = dataInicioGem;
    }

    public LocalDate getDataBase() {
        return dataBase;
    }

    public void setDataBase(LocalDate dataBase) {
        this.dataBase = dataBase;
    }

    public Long getDiasSemMovimentacao() {
        return diasSemMovimentacao;
    }

    public void setDiasSemMovimentacao(Long diasSemMovimentacao) {
        this.diasSemMovimentacao = diasSemMovimentacao;
    }
}