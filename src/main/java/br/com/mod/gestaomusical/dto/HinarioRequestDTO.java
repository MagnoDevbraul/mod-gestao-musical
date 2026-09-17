package br.com.mod.gestaomusical.dto;

import java.time.LocalDate;

public class HinarioRequestDTO {

    private Long alunoId;

    // Usuário que realizou o lançamento no MOD
    private Long usuarioId;

    // Usuário que autorizou o conteúdo musical
    private Long autorizadoPorUsuarioId;

    private LocalDate data;
    private Integer hino;
    private String voz;
    private String clave;
    private String observacoes;

    public HinarioRequestDTO() {
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

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}