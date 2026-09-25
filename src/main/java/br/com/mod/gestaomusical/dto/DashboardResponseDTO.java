package br.com.mod.gestaomusical.dto;

import java.util.List;

public class DashboardResponseDTO {

    private long totalAlunos;
    private long alunosAtivos;
    private long alunosArquivados;
    private long alteracoesPendentes;
    private long notificacoes;

    private List<DashboardAtividadeResponseDTO>
            atividadesRecentes;

    private List<DashboardAlunosPorComumResponseDTO>
            alunosPorComum;

    public DashboardResponseDTO() {
    }

    public long getTotalAlunos() {
        return totalAlunos;
    }

    public void setTotalAlunos(long totalAlunos) {
        this.totalAlunos = totalAlunos;
    }

    public long getAlunosAtivos() {
        return alunosAtivos;
    }

    public void setAlunosAtivos(long alunosAtivos) {
        this.alunosAtivos = alunosAtivos;
    }

    public long getAlunosArquivados() {
        return alunosArquivados;
    }

    public void setAlunosArquivados(long alunosArquivados) {
        this.alunosArquivados = alunosArquivados;
    }

    public long getAlteracoesPendentes() {
        return alteracoesPendentes;
    }

    public void setAlteracoesPendentes(long alteracoesPendentes) {
        this.alteracoesPendentes = alteracoesPendentes;
    }

    public long getNotificacoes() {
        return notificacoes;
    }

    public void setNotificacoes(long notificacoes) {
        this.notificacoes = notificacoes;
    }

    public List<DashboardAtividadeResponseDTO>
    getAtividadesRecentes() {

        return atividadesRecentes;
    }

    public void setAtividadesRecentes(
            List<DashboardAtividadeResponseDTO>
                    atividadesRecentes) {

        this.atividadesRecentes =
                atividadesRecentes;
    }

    public List<DashboardAlunosPorComumResponseDTO>
    getAlunosPorComum() {

        return alunosPorComum;
    }

    public void setAlunosPorComum(
            List<DashboardAlunosPorComumResponseDTO>
                    alunosPorComum) {

        this.alunosPorComum =
                alunosPorComum;
    }
}