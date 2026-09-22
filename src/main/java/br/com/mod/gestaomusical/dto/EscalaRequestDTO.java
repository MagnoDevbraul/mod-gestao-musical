package br.com.mod.gestaomusical.dto;

import java.time.LocalDate;

public class EscalaRequestDTO {

    private Long alunoId;

    /*
     * Campo opcional.
     *
     * Quando não informado, o próprio usuário autenticado
     * será utilizado como autorizador musical.
     *
     * Secretaria e Encarregado Regional podem indicar outro
     * usuário, sujeito às regras de perfil, setor e sessão ativa.
     */
    private Long autorizadoPorUsuarioId;

    private LocalDate data;
    private String nomeEscala;
    private Long tonalidadeId;
    private String clave;
    private String observacoes;

    public EscalaRequestDTO() {
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
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

    public String getNomeEscala() {
        return nomeEscala;
    }

    public void setNomeEscala(String nomeEscala) {
        this.nomeEscala = nomeEscala;
    }

    public Long getTonalidadeId() {
        return tonalidadeId;
    }

    public void setTonalidadeId(Long tonalidadeId) {
        this.tonalidadeId = tonalidadeId;
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