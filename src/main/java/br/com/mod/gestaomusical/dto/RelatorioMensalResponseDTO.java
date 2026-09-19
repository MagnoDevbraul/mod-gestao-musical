package br.com.mod.gestaomusical.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RelatorioMensalResponseDTO {

    private Long id;

    private LocalDate periodoInicio;
    private LocalDate periodoFim;

    private Integer totalAlunosAtivos;
    private Integer totalAlunosArquivados;

    private Integer totalMts;
    private Integer totalMsa;
    private Integer totalMetodo;
    private Integer totalHinario;
    private Integer totalEscala;

    private Integer totalAlunosSemMovimentacao60Dias;

    private Integer totalCompartilhamentos;
    private Integer totalExclusoesArquivamentos;
    private Integer totalRestauracoes;

    private Integer totalNotificacoes;
    private Integer totalEventosAuditoria;

    private LocalDateTime geradoEm;

    public RelatorioMensalResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPeriodoInicio() {
        return periodoInicio;
    }

    public void setPeriodoInicio(LocalDate periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    public LocalDate getPeriodoFim() {
        return periodoFim;
    }

    public void setPeriodoFim(LocalDate periodoFim) {
        this.periodoFim = periodoFim;
    }

    public Integer getTotalAlunosAtivos() {
        return totalAlunosAtivos;
    }

    public void setTotalAlunosAtivos(Integer totalAlunosAtivos) {
        this.totalAlunosAtivos = totalAlunosAtivos;
    }

    public Integer getTotalAlunosArquivados() {
        return totalAlunosArquivados;
    }

    public void setTotalAlunosArquivados(Integer totalAlunosArquivados) {
        this.totalAlunosArquivados = totalAlunosArquivados;
    }

    public Integer getTotalMts() {
        return totalMts;
    }

    public void setTotalMts(Integer totalMts) {
        this.totalMts = totalMts;
    }

    public Integer getTotalMsa() {
        return totalMsa;
    }

    public void setTotalMsa(Integer totalMsa) {
        this.totalMsa = totalMsa;
    }

    public Integer getTotalMetodo() {
        return totalMetodo;
    }

    public void setTotalMetodo(Integer totalMetodo) {
        this.totalMetodo = totalMetodo;
    }

    public Integer getTotalHinario() {
        return totalHinario;
    }

    public void setTotalHinario(Integer totalHinario) {
        this.totalHinario = totalHinario;
    }

    public Integer getTotalEscala() {
        return totalEscala;
    }

    public void setTotalEscala(Integer totalEscala) {
        this.totalEscala = totalEscala;
    }

    public Integer getTotalAlunosSemMovimentacao60Dias() {
        return totalAlunosSemMovimentacao60Dias;
    }

    public void setTotalAlunosSemMovimentacao60Dias(
            Integer totalAlunosSemMovimentacao60Dias) {
        this.totalAlunosSemMovimentacao60Dias =
                totalAlunosSemMovimentacao60Dias;
    }

    public Integer getTotalCompartilhamentos() {
        return totalCompartilhamentos;
    }

    public void setTotalCompartilhamentos(Integer totalCompartilhamentos) {
        this.totalCompartilhamentos = totalCompartilhamentos;
    }

    public Integer getTotalExclusoesArquivamentos() {
        return totalExclusoesArquivamentos;
    }

    public void setTotalExclusoesArquivamentos(
            Integer totalExclusoesArquivamentos) {
        this.totalExclusoesArquivamentos =
                totalExclusoesArquivamentos;
    }

    public Integer getTotalRestauracoes() {
        return totalRestauracoes;
    }

    public void setTotalRestauracoes(Integer totalRestauracoes) {
        this.totalRestauracoes = totalRestauracoes;
    }

    public Integer getTotalNotificacoes() {
        return totalNotificacoes;
    }

    public void setTotalNotificacoes(Integer totalNotificacoes) {
        this.totalNotificacoes = totalNotificacoes;
    }

    public Integer getTotalEventosAuditoria() {
        return totalEventosAuditoria;
    }

    public void setTotalEventosAuditoria(Integer totalEventosAuditoria) {
        this.totalEventosAuditoria = totalEventosAuditoria;
    }

    public LocalDateTime getGeradoEm() {
        return geradoEm;
    }

    public void setGeradoEm(LocalDateTime geradoEm) {
        this.geradoEm = geradoEm;
    }
}