package br.com.mod.gestaomusical.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MetodoResponseDTO {

    private Long id;

    private Long alunoId;
    private String alunoNome;

    private LocalDate data;
    private String nomeMetodo;

    private Integer paginaInicial;
    private Integer paginaFinal;

    private Integer licaoInicial;
    private Integer licaoFinal;

    private String clave;

    private Long autorizadoPorUsuarioId;
    private String autorizadoPorUsuarioNome;

    private String observacoes;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public MetodoResponseDTO() {
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

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getNomeMetodo() {
        return nomeMetodo;
    }

    public void setNomeMetodo(String nomeMetodo) {
        this.nomeMetodo = nomeMetodo;
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

    public Integer getLicaoInicial() {
        return licaoInicial;
    }

    public void setLicaoInicial(Integer licaoInicial) {
        this.licaoInicial = licaoInicial;
    }

    public Integer getLicaoFinal() {
        return licaoFinal;
    }

    public void setLicaoFinal(Integer licaoFinal) {
        this.licaoFinal = licaoFinal;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Long getAutorizadoPorUsuarioId() {
        return autorizadoPorUsuarioId;
    }

    public void setAutorizadoPorUsuarioId(Long autorizadoPorUsuarioId) {
        this.autorizadoPorUsuarioId = autorizadoPorUsuarioId;
    }

    public String getAutorizadoPorUsuarioNome() {
        return autorizadoPorUsuarioNome;
    }

    public void setAutorizadoPorUsuarioNome(String autorizadoPorUsuarioNome) {
        this.autorizadoPorUsuarioNome = autorizadoPorUsuarioNome;
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
}