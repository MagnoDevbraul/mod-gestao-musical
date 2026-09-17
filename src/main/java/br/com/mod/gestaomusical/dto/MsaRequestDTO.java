package br.com.mod.gestaomusical.dto;

import java.time.LocalDate;

public class MsaRequestDTO {

    private Long alunoId;

    // Usuário que realizou o lançamento no MOD
    private Long usuarioId;

    // Usuário que autorizou o conteúdo musical
    private Long autorizadoPorUsuarioId;

    private LocalDate data;
    private String fase;

    private Integer paginaInicial;
    private Integer paginaFinal;

    private Integer licaoInicial;
    private Integer licaoFinal;

    private String clave;
    private String observacoes;

    public MsaRequestDTO() {
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getAutorizadoPorUsuarioId() {
        return autorizadoPorUsuarioId;
    }

    public void setAutorizadoPorUsuarioId(Long autorizadoPorUsuarioId) {
        this.autorizadoPorUsuarioId = autorizadoPorUsuarioId;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
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

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}