package br.com.mod.gestaomusical.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "mts")
public class Mts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "modulo", nullable = false)
    private Integer modulo;

    @Column(name = "licao", nullable = false)
    private Integer licao;

    @Column(name = "pagina_inicial")
    private Integer paginaInicial;

    @Column(name = "pagina_final")
    private Integer paginaFinal;

    /*
     * Usuário responsável pela autorização musical.
     *
     * A coluna aceita NULL para preservar compatibilidade
     * com registros antigos de MTS criados antes desta regra.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autorizado_por_usuario_id")
    private Usuario autorizadoPorUsuario;

    @Column(name = "observacoes")
    private String observacoes;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    public Mts() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
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

    public Usuario getAutorizadoPorUsuario() {
        return autorizadoPorUsuario;
    }

    public void setAutorizadoPorUsuario(
            Usuario autorizadoPorUsuario) {

        this.autorizadoPorUsuario =
                autorizadoPorUsuario;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
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

    @PrePersist
    protected void aoCriar() {

        LocalDateTime agora =
                LocalDateTime.now();

        this.criadoEm = agora;
        this.atualizadoEm = agora;
    }

    @PreUpdate
    protected void aoAtualizar() {

        this.atualizadoEm =
                LocalDateTime.now();
    }
}