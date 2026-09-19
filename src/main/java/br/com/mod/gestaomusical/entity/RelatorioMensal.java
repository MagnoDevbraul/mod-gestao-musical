package br.com.mod.gestaomusical.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "relatorio_mensal",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_relatorio_mensal_periodo",
                        columnNames = {"periodo_inicio", "periodo_fim"}
                )
        }
)
public class RelatorioMensal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "periodo_inicio", nullable = false)
    private LocalDate periodoInicio;

    @Column(name = "periodo_fim", nullable = false)
    private LocalDate periodoFim;

    @Column(name = "total_alunos_ativos", nullable = false)
    private Integer totalAlunosAtivos = 0;

    @Column(name = "total_alunos_arquivados", nullable = false)
    private Integer totalAlunosArquivados = 0;

    @Column(name = "total_mts", nullable = false)
    private Integer totalMts = 0;

    @Column(name = "total_msa", nullable = false)
    private Integer totalMsa = 0;

    @Column(name = "total_metodo", nullable = false)
    private Integer totalMetodo = 0;

    @Column(name = "total_hinario", nullable = false)
    private Integer totalHinario = 0;

    @Column(name = "total_escala", nullable = false)
    private Integer totalEscala = 0;

    @Column(
            name = "total_alunos_sem_movimentacao_60_dias",
            nullable = false
    )
    private Integer totalAlunosSemMovimentacao60Dias = 0;

    @Column(name = "total_compartilhamentos", nullable = false)
    private Integer totalCompartilhamentos = 0;

    @Column(
            name = "total_exclusoes_arquivamentos",
            nullable = false
    )
    private Integer totalExclusoesArquivamentos = 0;

    @Column(name = "total_restauracoes", nullable = false)
    private Integer totalRestauracoes = 0;

    @Column(name = "total_notificacoes", nullable = false)
    private Integer totalNotificacoes = 0;

    @Column(name = "total_eventos_auditoria", nullable = false)
    private Integer totalEventosAuditoria = 0;

    @Column(name = "gerado_em", nullable = false)
    private LocalDateTime geradoEm;

    public RelatorioMensal() {
    }

    @PrePersist
    protected void aoCriar() {
        if (this.geradoEm == null) {
            this.geradoEm = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void aoAtualizar() {
        this.geradoEm = LocalDateTime.now();
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