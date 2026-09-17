package br.com.mod.gestaomusical.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class HinarioResponseDTO {

    private Long id;

    private Long alunoId;
    private String alunoNome;

    private LocalDate data;
    private Integer hino;
    private String voz;
    private String clave;

    private Long autorizadoPorUsuarioId;
    private String autorizadoPorUsuarioNome;

    private String observacoes;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public HinarioResponseDTO() {
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

    public Integer getHino() {
        return hino;
    }

    public void setHino(Integer hino) {
        this.hino = hino;
    }

    public String getVoz() {
        return voz;
    }

    public void setVoz(String voz) {
        this.voz = voz;
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